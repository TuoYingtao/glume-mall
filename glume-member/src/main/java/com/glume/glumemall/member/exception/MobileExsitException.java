package com.glume.glumemall.member.exception;

/**
 * @author tuoyingtao
 * @create 2021-12-07 10:56
 */
public class MobileExsitException extends RuntimeException {
    public MobileExsitException() {
        super("手机号存在！");
    }
}
