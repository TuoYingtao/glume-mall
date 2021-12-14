package com.glume.glumemall.glumecart.to;

import lombok.Data;

/**
 * @author tuoyingtao
 * @create 2021-12-13 11:28
 */
@Data
public class UserInfoTo {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户KEy
     */
    private String userKey;
    /**
     * 是否分配的有临时用户
     */
    private Boolean isTempUser = false;
}
