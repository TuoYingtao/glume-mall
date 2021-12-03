package com.glume.glumemall.product.vo.item;

import lombok.Data;

/**
 * 商品规格参数
 */
@Data
public class SpuBaseAttrVo {
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 属性值
     */
    private String attrValues;
}