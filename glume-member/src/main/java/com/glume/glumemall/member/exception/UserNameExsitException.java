package com.glume.glumemall.member.exception;

/**
 * @author tuoyingtao
 * @create 2021-12-07 10:58
 */
public class UserNameExsitException extends RuntimeException{
    public UserNameExsitException() {
        super("用户名已存在！");
    }
}
