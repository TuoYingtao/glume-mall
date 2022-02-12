package com.glume.glumemall.order.vo;

import com.glume.glumemall.order.entity.OrderEntity;
import lombok.Data;

/**
 * 下单成功返回的订单数据
 * @author tuoyingtao
 * @create 2022-02-12 16:30
 */
@Data
public class SubmitOrderResponseVo {

    /**
     * 订单信息
     */
    private OrderEntity order;
    /**
     * 订单状态 0-成功
     */
    private Integer code;
}
