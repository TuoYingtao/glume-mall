package com.glume.glumemall.admin.exception;

import com.glume.glumemall.admin.exception.servlet.ServiceException;
import com.glume.glumemall.common.constant.HttpStatus;
import com.glume.glumemall.common.utils.R;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * @author tuoyingtao
 * @create 2021-10-23 16:49
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return R.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * Token异常
     */
    @ExceptionHandler(JwtException.class)
    public R handleJwtException(JwtException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',认证失败'{}'", requestURI, e.getMessage());
        return R.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return R.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R handleServiceException(ServiceException e, HttpServletRequest request) {
        LOGGER.error(e.getMessage(), e);
        Integer code = e.getCode();
        return code == null ? R.error(code, e.getMessage()) : R.error(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',发生未知异常.", requestURI, e);
        return R.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',发生系统异常.", requestURI, e);
        return R.error(e.getMessage());
    }
}
