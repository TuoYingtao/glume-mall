package com.glume.glumemall.order.vo;

import com.glume.common.core.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单确认页需要的实体Vo
 * @author tuoyingtao
 * @create 2021-12-16 10:31
 */
public class OrderConfirmVo {

    /**
     * 会员地址 ums_member_receive_address
     */
    @Setter @Getter
    private List<MemberAddressVo> address;
    /**
     * 所选的购物项 pms_sku_info pms_sku_sale_attr_value
     */
    @Setter @Getter
    private List<OrderItemVo> items;
    /**
     * 优惠卷、积分 ums_member
     */
    @Setter @Getter
    private Integer integration;
    /**
     * 防重令牌
     */
    @Setter @Getter
    private String orderToken;
    /**
     * 订单总额
     */
    private BigDecimal total = new BigDecimal("0");
    /**
     * 应付价格
     */
    private BigDecimal payPrice = new BigDecimal("0");

    public BigDecimal getTotal() {
        if (StringUtils.isNotNull(items)) {
            for (OrderItemVo item : items) {
                BigDecimal decimal = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                total = total.add(decimal);
            }
        }
        return total;
    }

    public BigDecimal getPayPrice() {
        if (StringUtils.isNotNull(items)) {
            for (OrderItemVo item : items) {
                BigDecimal decimal = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                payPrice = payPrice.add(decimal);
            }
        }
        return payPrice;
    }
}
