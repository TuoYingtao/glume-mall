package com.glume.glumemall.glumecart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 * @author tuoyingtao
 * @create 2021-12-10 17:22
 */
@Data
public class Cart {

    /**
     * 商品项
     */
    private List<CartItem> items;
    /**
     * 商品类型数量
     */
    private Integer countType;
    /**
     * 商品项总数量
     */
    private Integer countNum;
    /**
     * 商品总价
     */
    private BigDecimal totalAmount;
    /**
     * 减免价格
     */
    private BigDecimal reduce = new BigDecimal("0.00");

    public Integer getCountType() {
        Integer count = 0;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                count += 1;
            }
        }
        return count;
    }

    public Integer getCountNum() {
        Integer count = 0;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        // 计算购物项总价
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                if (item.getCheck()) {
                    amount = amount.add(item.getTotalPrice());
                }
            }
        }
        BigDecimal subtract = amount.subtract(getReduce());
        return subtract;
    }

    public BigDecimal getReduce() {
        return reduce;
    }
}
