package com.glume.glumemall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.to.mq.SeckillOrderTo;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.seckill.feign.CouponFeignService;
import com.glume.glumemall.seckill.feign.ProductFeignService;
import com.glume.glumemall.seckill.interceptor.LoginUserInterceptor;
import com.glume.glumemall.seckill.service.SeckillService;
import com.glume.glumemall.seckill.to.SeckillSkuRedisTo;
import com.glume.glumemall.seckill.vo.SeckillSessionsWithSkusVo;
import com.glume.glumemall.seckill.vo.SkuInfoVo;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 上传秒杀活动
 * @author tuoyingtao
 * @create 2022-03-01 17:20
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";

    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    @Override
    public void uploadSeckillSkuLatest3Day() {
        // 扫描最近三天需要参与秒杀的活动
        R result = couponFeignService.getLates3DaySession();
        if (result.getCode() == HttpStatus.SUCCESS) {
            // 需要上架的商品
            List<SeckillSessionsWithSkusVo> data = result.getData(new TypeReference<List<SeckillSessionsWithSkusVo>>() {
            });
            // 缓存活动信息
            cacheSessionInfo(data);
            // 缓存活动关联的商品信息
            cacheSessionSkuRelationInfo(data);
        }
    }

    /** 返回当前时间可以参与的秒杀商品 */
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        // 确定当前时间属于哪个秒杀场次
        long time = new Date().getTime();
        List<SeckillSkuRedisTo> collect = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(SESSION_CACHE_PREFIX + "*");
        for (String key : keys) {
            String replace = key.replace(SESSION_CACHE_PREFIX, "");
            String[] s = replace.split("_");
            long startTime = Long.parseLong(s[0]);
            long endTime = Long.parseLong(s[1]);
            if (time >= startTime && time <= endTime) {
                List<String> range = redisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                List<String> list = hashOps.multiGet(range);
                if (StringUtils.isNotEmpty(list)) {
                    List<SeckillSkuRedisTo> toList = list.stream().map(item -> {
                        SeckillSkuRedisTo skuRedisTo = JSON.parseObject(item, SeckillSkuRedisTo.class);
                        return skuRedisTo;
                    }).collect(Collectors.toList());
                    collect.addAll(toList);
                }
            }
        }
        return collect;
    }

    /** 获取某一个商品的秒杀信息 */
    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        if (StringUtils.isNotEmpty(keys)) {
            String regx = "\\d_" + skuId;
            for (String key : keys) {
                if (Pattern.matches(regx, key)) {
                    String json = hashOps.get(key);
                    SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
                    // 随机码处理，只有在活动时间范围内才返回
                    long current = new Date().getTime();
                    Long starTime = seckillSkuRedisTo.getStarTime();
                    Long endTime = seckillSkuRedisTo.getEndTime();
                    if (!(current >= starTime && current <= endTime)) {
                        seckillSkuRedisTo.setRandomCode(null);
                    }
                    return seckillSkuRedisTo;
                }
            }
        }
        return null;
    }

    @Override
    public String kill(String killId, String key, Integer num) {
        // 获取当前秒杀商品信息
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String json = hashOps.get(killId);
        if (StringUtils.isEmpty(json)) return null;
        SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
        // 校验合法性
        Long starTime = seckillSkuRedisTo.getStarTime();
        Long endTime = seckillSkuRedisTo.getEndTime();
        long current = new Date().getTime();
        if (current >= starTime && current <= endTime) {
            // 校验随机码
            String randomCode = seckillSkuRedisTo.getRandomCode();
            String skuId = seckillSkuRedisTo.getPromotionSessionId() + "_" + seckillSkuRedisTo.getSkuId();
            if (randomCode.equals(key) && skuId.equals(killId)) {
                // 验证用户购买商品的数量
                if (num <= seckillSkuRedisTo.getSeckillLimit().intValue()) {
                    // 验证用户是否已经购买过此类商品，幂等性处理，保持每位用户只能购买一次
                    String userRedisKey = LoginUserInterceptor.toThreadLocal.get().getId() + "_" + skuId;
                    long ttl = endTime - current;
                    Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(userRedisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                    if (ifAbsent) {
                        // 占位成功，当前用户没有买过此类商品；扣减信号量
                        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                        try {
                            boolean acquire = semaphore.tryAcquire(num, 100, TimeUnit.MILLISECONDS);
                            if (acquire) {
                                // 秒杀成功
                                String orderSn = IdWorker.getTimeId();
                                SeckillOrderTo seckillOrderTo = new SeckillOrderTo();
                                seckillOrderTo.setOrderSn(orderSn);
                                seckillOrderTo.setPromotionSessionId(seckillSkuRedisTo.getPromotionSessionId());
                                seckillOrderTo.setSkuId(seckillSkuRedisTo.getSkuId());
                                seckillOrderTo.setNum(num);
                                seckillOrderTo.setSeckillPrice(seckillSkuRedisTo.getSeckillPrice());
                                seckillOrderTo.setMemberId(LoginUserInterceptor.toThreadLocal.get().getId());
                                rabbitTemplate.convertAndSend("order-event-exchange","order.seckill.order",seckillOrderTo);
                                return orderSn;
                            }
                        } catch (InterruptedException e) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    /** 缓存活动信息 */
    private void cacheSessionInfo(List<SeckillSessionsWithSkusVo> sessionsWithSkusVos) {
        sessionsWithSkusVos.forEach(sessionsWithSkusVo -> {
            long startTime = sessionsWithSkusVo.getStartTime().getTime();
            long endTime = sessionsWithSkusVo.getEndTime().getTime();
            String key = SESSION_CACHE_PREFIX + startTime + "_" + endTime;
            // 幂等性：当缓存中没有当前数据时，才进行保存数据。
            Boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey) {
                List<String> value = sessionsWithSkusVo.getRelationSkus()
                        .stream().map(item -> item.getPromotionSessionId() + "_" + item.getSkuId()).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key,value);
            }
        });
    }

    /** 缓存活动关联的商品信息 */
    private void cacheSessionSkuRelationInfo(List<SeckillSessionsWithSkusVo> sessionsWithSkusVos) {
        sessionsWithSkusVos.forEach(sessionsWithSkusVo -> {
            // 准备 Hash 操作
            BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            sessionsWithSkusVo.getRelationSkus().forEach(seckillSkuRelationWithVo -> {
                // 使用活动场次ID + 活动商品ID 作为键，避免多个活动同样的商品不进行缓存问题
                String key = seckillSkuRelationWithVo.getPromotionSessionId() + "_" + seckillSkuRelationWithVo.getSkuId();
                // 幂等性：当缓存中没有当前数据时，才进行保存数据。
                if (!hashOps.hasKey(key)) {
                    SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();
                    // Sku的基本数据
                    R skuInfo = productFeignService.getSkuInfo(seckillSkuRelationWithVo.getSkuId());
                    if (skuInfo.getCode() == HttpStatus.SUCCESS) {
                        SkuInfoVo data = skuInfo.getData(new TypeReference<SkuInfoVo>() {
                        });
                        seckillSkuRedisTo.setSkuInfoVo(data);
                    }
                    // sku的秒杀信息
                    BeanUtils.copyProperties(seckillSkuRelationWithVo,seckillSkuRedisTo);
                    // 设置当前商品的秒杀时间信息
                    seckillSkuRedisTo.setStarTime(sessionsWithSkusVo.getStartTime().getTime());
                    seckillSkuRedisTo.setEndTime(sessionsWithSkusVo.getEndTime().getTime());
                    // 商品随机码
                    String code = UUID.randomUUID().toString().replace("-", "");
                    seckillSkuRedisTo.setRandomCode(code);
                    // 将活动商品信息以JSON格式缓存到Redis中
                    String jsonString = JSON.toJSONString(seckillSkuRedisTo);
                    hashOps.put(key,jsonString);

                    // 分布式信号量 使用商品可以秒杀的数量作为信号量
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + code);
                    semaphore.trySetPermits(seckillSkuRelationWithVo.getSeckillCount().intValue());
                }
            });
        });
    }
}
