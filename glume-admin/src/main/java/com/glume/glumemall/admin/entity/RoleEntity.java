package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 角色
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@Data
@TableName("sys_role")
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@NotNull(message = "角色ID不能为空",groups = UpdateGroup.class)
	@Null(message = "添加不能指定角色ID",groups = AddGroup.class)
	private Long roleId;
	/**
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空",
			groups = {UpdateGroup.class,AddGroup.class})
	private String roleName;
	/**
	 * 角色标签
	 */
	@NotBlank(message = "角色标签不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	private String roleTag;
	/**
	 * 备注
	 */
	@Length(min = 2,max = 100,message = "备注长度不能超过2到100个字符",
			groups = {AddGroup.class,UpdateGroup.class})
	private String remark;
	/**
	 * 角色菜单ID
	 */
	@TableField(exist = false)
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	private Long[] menuIds;
	/**
	 * 创建者ID
	 */
	@JsonIgnore
	private Long createUserId;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

}
