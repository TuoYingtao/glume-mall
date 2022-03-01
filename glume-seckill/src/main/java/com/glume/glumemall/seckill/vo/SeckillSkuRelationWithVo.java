package com.glume.glumemall.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 秒杀活动商品关联
 * @author TuoYingtao
 * @create 2022-03-01 22:19
 */
@Data
public class SeckillSkuRelationWithVo {

    /**
     * id
     */
    private Long id;
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
}
