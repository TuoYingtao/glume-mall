package com.glume.glumemall.product.vo.item;

import lombok.Data;

/**
 * 商品销售属性详情
 */
@Data
public class ItemSaleAttrsVo {
    /**
     * 属性ID
     */
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 属性值
     */
    private String attrValues;

}