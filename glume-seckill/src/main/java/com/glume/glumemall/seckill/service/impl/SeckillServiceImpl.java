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

    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";

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
            List<String> value = sessionsWithSkusVo.getRelationSkus()
                    .stream().map(item -> item.getSkuId().toString()).collect(Collectors.toList());
            redisTemplate.opsForList().leftPushAll(key,value);
        });
    }

    /** 缓存活动关联的商品信息 */
    private void cacheSessionSkuRelationInfo(List<SeckillSessionsWithSkusVo> sessionsWithSkusVos) {
        sessionsWithSkusVos.forEach(sessionsWithSkusVo -> {
            // 准备 Hash 操作
            BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            sessionsWithSkusVo.getRelationSkus().forEach(seckillSkuRelationWithVo -> {
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
                seckillSkuRedisTo.setRandomCode(UUID.randomUUID().toString().replace("-",""));
                String jsonString = JSON.toJSONString(seckillSkuRelationWithVo);
                hashOps.put(seckillSkuRelationWithVo.getId(),jsonString);
            });
        });
    }
}
