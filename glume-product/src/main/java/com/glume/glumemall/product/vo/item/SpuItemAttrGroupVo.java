package com.glume.glumemall.product.vo.item;

import lombok.Data;

import java.util.List;

/**
 * 商品规格参数
 */
@Data
public class SpuItemAttrGroupVo {
    /**
     * 分组名
     */
    private String groupName;
    /**
     * 分组信息
     */
    private List<SpuBaseAttrVo> attrs;

}