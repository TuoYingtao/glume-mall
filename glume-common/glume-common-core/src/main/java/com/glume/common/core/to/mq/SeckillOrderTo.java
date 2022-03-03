package com.glume.common.core.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 秒杀订单
 * @author tuoyingtao
 * @create 2022-03-03 17:31
 */
@Data
public class SeckillOrderTo {

    /**
     * 订单号
     */
    private String orderSn;
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
    private Integer num;
    /**
     * 会员ID
     */
    private Long memberId;
}
