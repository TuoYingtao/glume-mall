package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import lombok.Data;

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
	private Long menuId;
	/**
	 * 父菜单ID，一级菜单为0
	 */
	private Long parentId;
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 菜单path
	 */
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
	private Character visible;
	/**
	 * 菜单状态（0正常 1停用）
	 */
	private Character status;
	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;
	/**
	 * 菜单类型（M目录 C菜单 F按钮）
	 */
	private Character menuType;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新者
	 */
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
	private List<MenuEntity> children;

}
