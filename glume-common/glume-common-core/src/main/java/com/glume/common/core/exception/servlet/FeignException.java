package com.glume.common.core.exception.servlet;

import java.io.Serializable;

/**
 * @author tuoyingtao
 * @create 2022-03-12 17:54
 */
public class FeignException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;
    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    public FeignException() {
    }

    public FeignException(Integer code, String message, String detailMessage) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
    }

    public FeignException(String message, Throwable cause, Integer code, String message1, String detailMessage) {
        super(message, cause);
        this.code = code;
        this.message = message1;
        this.detailMessage = detailMessage;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
