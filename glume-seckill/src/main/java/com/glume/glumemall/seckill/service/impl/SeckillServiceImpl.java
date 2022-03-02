package com.glume.glumemall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.glumemall.seckill.feign.CouponFeignService;
import com.glume.glumemall.seckill.feign.ProductFeignService;
import com.glume.glumemall.seckill.service.SeckillService;
import com.glume.glumemall.seckill.to.SeckillSkuRedisTo;
import com.glume.glumemall.seckill.vo.SeckillSessionsWithSkusVo;
import com.glume.glumemall.seckill.vo.SkuInfoVo;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
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
