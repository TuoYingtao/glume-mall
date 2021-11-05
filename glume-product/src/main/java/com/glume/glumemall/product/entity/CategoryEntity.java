package com.glume.glumemall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glume.common.core.valid.AddGroup;
import com.glume.common.core.valid.UpdateGroup;
import com.glume.common.core.valid.IDGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 商品三级分类
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	@NotNull(message = "商品ID不能为空",
			groups = {UpdateGroup.class, IDGroup.class})
	@Null(message = "新增不能指定ID",
			groups = AddGroup.class)
	private Long catId;
	/**
	 * 分类名称
	 */
	@NotBlank(message = "分类名称不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private String name;
	/**
	 * 父分类id
	 */
	@NotNull(message = "父分类ID不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private Long parentCid;
	/**
	 * 层级
	 */
	@NotNull(message = "层级不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private Integer catLevel;
	/**
	 * 是否显示[0-不显示，1显示]
	 */
	@JsonIgnore
	@TableLogic
	private Integer showStatus;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private Integer sort;
	/**
	 * 图标地址
	 */
	@NotBlank(message = "图标地址不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private String icon;
	/**
	 * 计量单位
	 */
	private String productUnit;
	/**
	 * 商品数量
	 */
	@NotNull(message = "商品数量不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private Integer productCount;
	/**
	 * 子级
	 */
	@TableField(exist = false)
	private List<CategoryEntity> children;
}
