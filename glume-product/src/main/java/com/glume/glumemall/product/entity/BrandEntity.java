package com.glume.glumemall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名称不能为空")
	@Size(min = 2,max = 18,message = "品牌名称必须是2~18个字符之间")
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "品牌logo不能为空")
	private String logo;
	/**
	 * 介绍
	 */
	@NotBlank(message = "描述不能为空")
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
	@NotBlank(message = "检索首字母不能为空")
	@Length(max = 2,message = "检索首字母长度不能超过两个字符")
	private String firstLetter;
	/**
	 * 排序
	 */
	private Integer sort;

}
