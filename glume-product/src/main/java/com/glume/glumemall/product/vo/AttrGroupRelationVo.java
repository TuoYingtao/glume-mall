package com.glume.glumemall.product.vo;

import lombok.Data;

/**
 * 分组关系实体
 * @author TuoYingtao
 * @create 2021-11-16 21:12
 */
@Data
public class AttrGroupRelationVo {

    /**
     * 属性ID
     */
    private Long attrId;
    /**
     * 分组ID
     */
    private Long attrGroupId;
}
