package com.glume.glumemall.authserver.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 注册信息VO
 * @author tuoyingtao
 * @create 2021-12-06 16:10
 */
public class UserRegisterVo {
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空！")
    @Length(min = 6,max = 18,message = "用户名必须在6-18位字符！")
    private String userName;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空！")
    @Length(min = 6,message = "密码长度不能小于6位！")
    private String password;
    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空！")
    @Pattern(regexp = "^[1][3-9][0-9]{9}$",message = "手机号格式不正确！")
    private String mobile;
    /**
     * 验证码
     */
    @NotEmpty(message = "验证码不能为空！")
    private String code;
}
