package com.glume.glumemall.seckill.to;

import com.glume.glumemall.seckill.vo.SkuInfoVo;

import java.math.BigDecimal;

/**
 * 活动秒杀商品信息与商品的详细信息
 * @author TuoYingtao
 * @create 2022-03-01 22:45
 */
public class SeckillSkuRedisTo {

    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

    // 商品的详细信息
    private SkuInfoVo skuInfoVo;

}
