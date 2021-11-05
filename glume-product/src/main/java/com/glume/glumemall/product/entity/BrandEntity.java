package com.glume.glumemall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glume.common.core.valid.AddGroup;
import com.glume.common.core.valid.IDGroup;
import com.glume.common.core.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(message = "品牌ID不能为空",
			groups = {AddGroup.class, IDGroup.class})
	@Null(message = "添加不能指定ID",
			groups = AddGroup.class)
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名称不能为空",
			groups = {AddGroup.class, UpdateGroup.class})
	@Size(min = 2,max = 18,message = "品牌名称必须是2~18个字符之间",
			groups = {AddGroup.class,UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "品牌logo不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@URL(message = "品牌logo地址不合法",
			groups = {AddGroup.class,UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	@NotBlank(message = "描述不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@JsonIgnore
	@TableLogic
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "检索首字母不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@Pattern(regexp = "/^[a-zA-Z]$/",message = "检索首字母必需是一个字母",
			groups = {AddGroup.class,UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@Min(value = 0,message = "排序必须大于等于0",
			groups = {AddGroup.class,UpdateGroup.class})
	private Integer sort;

}
