package com.glume.glumemall.seckill.service;

import com.glume.glumemall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @author tuoyingtao
 * @create 2022-03-01 17:20
 */
public interface SeckillService {

    void uploadSeckillSkuLatest3Day();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();
}
