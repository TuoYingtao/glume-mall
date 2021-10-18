package com.glume.glumemall.admin.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author tuoyingtao
 * @create 2021-10-18 17:22
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg) {
        super(msg);
    }
}
