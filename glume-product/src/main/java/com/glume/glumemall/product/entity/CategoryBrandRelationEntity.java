package com.glume.glumemall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.glume.common.core.annotation.valid.IDGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	@NotNull(message = "品牌&分类关联ID不能为空",groups = IDGroup.class)
	private Long id;
	/**
	 * 品牌ID
	 */
	@NotNull(message = "品牌ID不能为空")
	private Long brandId;
	/**
	 * 分类ID
	 */
	@NotNull(message = "分类ID不能为空")
	private Long catelogId;
	/**
	 * 品牌名称
	 */
	@NotBlank(message = "品牌名称不能为空")
	private String brandName;
	/**
	 * 分类名称
	 */
	@NotBlank(message = "分类名称不能为空")
	private String catelogName;
}
