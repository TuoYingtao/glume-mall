package com.glume.glumemall.order.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物项
 * @author tuoyingtao
 * @create 2021-12-16 10:40
 */
public class OrderItemVo {

    private Long skuId;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片
     */
    private String image;
    /**
     * SKU选中的属性
     */
    private List<String> skuAttr;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 计算购物车总价
     * @return
     */
    public BigDecimal getTotalPrice() {
        BigDecimal multiply = price.multiply(new BigDecimal(count.toString()));
        return multiply;
    }
}