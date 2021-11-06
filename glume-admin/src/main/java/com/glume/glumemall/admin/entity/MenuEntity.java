package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.glume.common.core.annotation.LogicStr;
import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.Logic;
import com.glume.common.core.annotation.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 菜单管理
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@Data
@TableName("sys_menu")
public class MenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@NotNull(message = "菜单ID不能为空",
			groups = {UpdateGroup.class, IDGroup.class})
	@Null(message = "新增不能指定ID",
			groups = AddGroup.class)
	private Long menuId;
	/**
	 * 父菜单ID，一级菜单为0
	 */
	@NotNull(message = "父菜单ID不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private Long parentId;
	/**
	 * 菜单名称
	 */
	@NotBlank(message = "菜单名称不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@Length(min = 2,max = 8,message = "菜单名称长度不能超过2到8个字符",
			groups = {AddGroup.class,UpdateGroup.class})
	private String name;
	/**
	 * 菜单path
	 */
	@NotBlank(message = "菜单地址不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private String path;
	/**
	 * 菜单路径
	 */
	private String component;
	/**
	 * 路由参数
	 */
	private String query;
	/**
	 * 菜单状态（0显示 1隐藏）
	 */
	@Logic(vals = {0,1},
			groups = {AddGroup.class,UpdateGroup.class})
	@NotNull(message = "菜单显示状态不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private Integer visible;
	/**
	 * 菜单状态（0正常 1停用）
	 */
	@Logic(vals = {0,1},
			groups = {AddGroup.class,UpdateGroup.class})
	@NotNull(message = "菜单启用状态不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private Integer status;
	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;
	/**
	 * 菜单类型（M目录 C菜单 F按钮）
	 */
	@NotNull(message = "菜单类型不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@LogicStr(vals = {"M","C","F"},message = "菜单类型必需是：M(目录)、C(菜单)、F(按钮)中的任意一个字母",
			groups = {AddGroup.class,UpdateGroup.class})
	private Character menuType;
	/**
	 * 菜单图标
	 */
	@NotBlank(message = "菜单图标不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private String icon;
	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 创建者
	 */
	@JsonIgnore
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新者
	 */
	@JsonIgnore
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 子级菜单
	 */
	@TableField(exist = false)
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	private List<MenuEntity> children;

}
