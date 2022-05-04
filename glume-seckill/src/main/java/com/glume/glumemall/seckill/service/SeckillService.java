package com.glume.glumemall.seckill.service;

import com.glume.glumemall.seckill.to.SeckillSkuRedisTo;
import com.glume.glumemall.seckill.vo.SeckillSessionsWithSkusVo;

import java.util.List;

/**
 * @author tuoyingtao
 * @create 2022-03-01 17:20
 */
public interface SeckillService {

    void uploadSeckillSkuLatest3Day();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num);

    List<SeckillSkuRedisTo> getNotCurrentSeckillSkus();
}
