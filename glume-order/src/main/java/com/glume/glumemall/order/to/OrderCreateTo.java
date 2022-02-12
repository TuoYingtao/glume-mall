package com.glume.glumemall.order.to;

import com.glume.glumemall.order.entity.OrderEntity;
import com.glume.glumemall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单
 * @author tuoyingtao
 * @create 2022-02-12 17:16
 */
@Data
public class OrderCreateTo {

    /**
     * 订单实体类
     */
    private OrderEntity order;
    /**
     * 订单项
     */
    private List<OrderItemEntity> orderItems;
    /**
     * 订单应付价格
     */
    private BigDecimal payPrice;
    /**
     * 订单运费价格
     */
    private BigDecimal fare;

}
