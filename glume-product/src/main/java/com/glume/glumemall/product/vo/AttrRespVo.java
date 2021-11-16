package com.glume.glumemall.product.vo;

import com.glume.glumemall.product.entity.AttrEntity;
import lombok.Data;



/**
 * 商品属性响应
 * @author tuoyingtao
 * @create 2021-11-16 10:44
 */
@Data
public class AttrRespVo extends AttrEntity {

    /**
     * 分类名
     */
    private String catelogName;
    /**
     * 组名ID
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
}
