package com.glume.glumemall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单确认页需要的实体Vo
 * @author tuoyingtao
 * @create 2021-12-16 10:31
 */
@Data
public class OrderConfirmVo {

    /**
     * 会员地址 ums_member_receive_address
     */
    private List<MemberAddressVo> address;
    /**
     * 所选的购物项 pms_sku_info pms_sku_sale_attr_value
     */
    private List<OrderItemVo> items;
    /**
     * 积分 ums_member
     */
    private Integer integration;
    /**
     * 订单总额
     */
    private BigDecimal total;
    /**
     * 应付价格
     */
    private BigDecimal payPrice;
}
