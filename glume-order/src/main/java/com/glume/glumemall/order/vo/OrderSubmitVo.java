package com.glume.glumemall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单提交数据Vo
 * @author tuoyingtao
 * @create 2022-02-12 16:13
 */
@Data
public class OrderSubmitVo {

    /**
     * 收货地址ID
     */
    private Long addrId;
    /**
     * 支付方式
     */
    private Integer payType;
    /**
     * 防重令牌
     */
    private String orderToken;
    /**
     * 应付价格 验价
     */
    private BigDecimal payPrice;
}
