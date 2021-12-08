package com.glume.glumemall.authserver.vo;

import lombok.Data;

/**
 * 登录Vo
 * @author tuoyingtao
 * @create 2021-12-08 9:40
 */
@Data
public class UserLoginVo {

    /**
     * 登录账号
     */
    private String loginacct;
    /**
     * 密码
     */
    private String password;
}
