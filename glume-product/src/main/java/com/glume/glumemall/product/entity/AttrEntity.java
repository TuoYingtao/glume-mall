package com.glume.glumemall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.glume.common.core.annotation.Logic;
import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * 商品属性
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
@Data
@TableName("pms_attr")
public class AttrEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 属性id
	 */
	@TableId
	@NotNull(message = "属性ID不能为空",groups = {UpdateGroup.class, IDGroup.class})
	@Null(message = "不能指定属性ID",groups = AddGroup.class)
	private Long attrId;
	/**
	 * 属性名
	 */
	@NotBlank(message = "属性名不能为空", groups = {UpdateGroup.class,IDGroup.class})
	private String attrName;
	/**
	 * 是否需要检索[0-不需要，1-需要]
	 */
	@Logic(vals = {0,1}, message = "格式错误：是否需要检索[0-不需要，1-需要]")
	@NotNull(message = "属性名不能为空", groups = {UpdateGroup.class,IDGroup.class})
	private Integer searchType;
	/**
	 * 值类型[0-为单个值，1-可以选择多个值]
	 */
	@Logic(vals = {0,1}, message = "格式错误：值类型[0-为单个值，1-可以选择多个值]")
	@NotNull(message = "值类型不能为空", groups = {UpdateGroup.class,IDGroup.class})
	private Integer valueType;
	/**
	 * 属性图标
	 */
	@NotBlank(message = "属性图标不能为空", groups = {UpdateGroup.class,IDGroup.class})
	private String icon;
	/**
	 * 可选值列表[用逗号分隔]
	 */
	@NotEmpty(message = "可选值列表不能为空", groups = {UpdateGroup.class,IDGroup.class})
	private String valueSelect;
	/**
	 * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
	 */
	@Logic(vals = {0,1,2}, message = "格式错误：属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]")
	@NotNull(message = "属性类型不能为空", groups = {UpdateGroup.class,IDGroup.class})
	private Integer attrType;
	/**
	 * 启用状态[0 - 禁用，1 - 启用]
	 */
	@Logic(vals = {0,1}, message = "格式错误：启用状态[0 - 禁用，1 - 启用]")
	@NotNull(message = "启用状态不能为空", groups = {UpdateGroup.class,IDGroup.class})
	private Long enable;
	/**
	 * 所属分类
	 */
	private Long catelogId;
	/**
	 * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
	 */
	@Logic(vals = {0,1}, message = "格式错误：快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整")
	@NotNull(message = "快速展示不能为空", groups = {UpdateGroup.class,IDGroup.class})
	private Integer showDesc;

}
