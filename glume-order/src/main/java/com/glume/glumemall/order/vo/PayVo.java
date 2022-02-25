package com.glume.glumemall.order.vo;

import lombok.Data;

/**
 * 支付实体Vo
 * @author tuoyingtao
 * @create 2022-2-25 16:38
 */
@Data
public class PayVo {
    /**
     * 商户订单号 必填
     */
    private String out_trade_no;
    /**
     * 订单名称 必填
     */
    private String subject;
    /**
     * 付款金额 必填
     */
    private String total_amount;
    /**
     * 商品描述 可空
     */
    private String body;
}
