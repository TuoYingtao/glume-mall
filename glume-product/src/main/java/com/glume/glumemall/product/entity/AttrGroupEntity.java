package com.glume.glumemall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * 属性分组
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
@Data
@TableName("pms_attr_group")
public class AttrGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分组id
	 */
	@TableId
	@NotNull(message = "分组ID不能为空",groups = {UpdateGroup.class, IDGroup.class})
	@Null(message = "新增数据不能指定ID",groups = AddGroup.class)
	private Long attrGroupId;
	/**
	 * 组名
	 */
	@NotBlank(message = "组名不能为空",groups = {AddGroup.class,UpdateGroup.class})
	@Length(min = 2,max = 26,message = "组名的长度必须是2~26个字符之间",
			groups = {AddGroup.class,UpdateGroup.class})
	private String attrGroupName;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为空",groups = {AddGroup.class,UpdateGroup.class})
	@Min(value = 0,message = "排序不能小于0",
			groups = {AddGroup.class,UpdateGroup.class})
	private Integer sort;
	/**
	 * 描述
	 */
	@NotBlank(message = "描述不能为空",groups = {AddGroup.class,UpdateGroup.class})
	@Length(min = 2,max = 255,message = "组名的长度必须是2~255个字符之间",
			groups = {AddGroup.class,UpdateGroup.class})
	private String descript;
	/**
	 * 组图标
	 */
	@NotBlank(message = "组图标不能为空",groups = {AddGroup.class,UpdateGroup.class})
	private String icon;
	/**
	 * 所属分类id
	 */
	@NotNull(message = "所属分类ID不能为空",groups = {AddGroup.class,UpdateGroup.class})
	private Long catelogId;
	/**
	 * 分类路径
	 */
	@TableField(exist = false)
	private List<CategoryEntity> categoryPath;

}
