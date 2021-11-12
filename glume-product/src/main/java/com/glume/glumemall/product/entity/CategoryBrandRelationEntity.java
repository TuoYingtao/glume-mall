package com.glume.glumemall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌&分类关联
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
@Data
@TableName("pms_category_brand_relation")
public class CategoryBrandRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 品牌ID
	 */
	private Long brandId;
	/**
	 * 分能ID
	 */
	private Long catelogId;
	/**
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 分类名称
	 */
	private String catelogName;

}
