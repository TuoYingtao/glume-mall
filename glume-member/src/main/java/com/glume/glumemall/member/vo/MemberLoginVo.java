package com.glume.glumemall.member.vo;

import lombok.Data;

/**
 * 登录Vo
 * @author tuoyingtao
 * @create 2021-12-08 9:51
 */
@Data
public class MemberLoginVo {
    /**
     * 登录账号
     */
    private String loginacct;
    /**
     * 密码
     */
    private String password;
}
