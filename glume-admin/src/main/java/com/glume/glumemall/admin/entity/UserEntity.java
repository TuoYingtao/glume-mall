package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.Logic;
import com.glume.common.core.annotation.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

/**
 * 系统用户
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@Data
@TableName("sys_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@NotNull(message = "用户ID不能为空",
			groups = {UpdateGroup.class, IDGroup.class})
	@Null(message = "新增不能指定ID",
			groups = AddGroup.class)
	private Long userId;
	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@Length(min = 2, max = 6, message = "姓名必须在2到6之间",
			groups = {AddGroup.class,UpdateGroup.class})
	private String username;
	/**
	 * 密码
	 */
	@JsonIgnore
	@NotBlank(message = "密码不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@Length(min = 6, max = 16,message = "密码必须在6到16位之间",
			groups = {AddGroup.class,UpdateGroup.class})
	private String password;
	/**
	 * 盐
	 */
	@JsonIgnore
	private String salt;
	/**
	 * 邮箱
	 */
	@NotBlank(message = "邮箱不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@Email(message = "邮箱格式不正确",
			groups = {AddGroup.class,UpdateGroup.class})
	@Size(min = 0, max = 50, message = "邮箱长度不能超过30个字符",
			groups = {AddGroup.class,UpdateGroup.class})
	private String email;
	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误",
			groups = {AddGroup.class,UpdateGroup.class})
	@Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符",
			groups = {AddGroup.class,UpdateGroup.class})
	private String mobile;
	/**
	 * 状态  0：禁用   1：正常
	 */
	@NotNull(message = "状态不能为空",
			groups = {AddGroup.class,UpdateGroup.class})
	@Logic(vals = {0,1}, groups = {AddGroup.class,UpdateGroup.class})
	private Integer status;
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
