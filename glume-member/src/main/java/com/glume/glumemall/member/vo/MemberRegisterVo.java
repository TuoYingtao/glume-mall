package com.glume.glumemall.member.vo;

import lombok.Data;

/**
 * 会员注册Vo
 * @author tuoyingtao
 * @create 2021-12-07 10:39
 */
@Data
public class MemberRegisterVo {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String mobile;
}
