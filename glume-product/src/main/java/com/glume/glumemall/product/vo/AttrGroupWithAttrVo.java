package com.glume.glumemall.product.vo;


import com.glume.glumemall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * 分组与属性
 * @author TuoYingtao
 * @create 2021-11-19 21:15
 */
@Data
public class AttrGroupWithAttrVo {

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 属性集合
     */
    private List<AttrEntity> attrs;
}
