package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
	@JsonProperty(value = "id")
	private Long userId;
	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空")
	@Length(min = 2, max = 6, message = "姓名必须在2到6之间")
	private String username;
	/**
	 * 密码
	 */
	@JsonIgnore
	@NotBlank(message = "密码不能为空")
	@Length(min = 6, max = 16,message = "密码必须在6到16位之间")
	private String password;
	/**
	 * 盐
	 */
	@JsonIgnore
	private String salt;
	/**
	 * 邮箱
	 */
	@NotBlank(message = "邮箱不能为空")
	@Email(message = "邮箱格式不正确")
	@Size(min = 0, max = 50, message = "邮箱长度不能超过30个字符")
	private String email;
	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	@Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
	private String mobile;
	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;
	/**
	 * 创建者ID
	 */
	@JsonIgnore
	private Long createUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
