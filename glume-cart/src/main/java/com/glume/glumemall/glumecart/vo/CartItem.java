package com.glume.glumemall.glumecart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物商品项
 * @author tuoyingtao
 * @create 2021-12-10 17:23
 */
@Data
public class CartItem {

    private Long skuId;
    /**
     * 是否选中
     */
    private Boolean check = true;
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
