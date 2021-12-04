package com.glume.common.core.exception;


import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.R;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
        return R.error(HttpStatus.BAD_METHOD,"不支持" + e.getMessage() + "请求");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R handleServiceException(ServiceException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',业务异常'{}'",requestURI, e.getMessage(), e);
        Integer code = e.getCode();
        return code == null ? R.error(e.getMessage()) : R.error(code, e.getMessage());
    }

    /**
     * 缺少所需的请求体异常处理
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R handlerHttpMessageNotReadableException(HttpMessageNotReadableException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',缺少所需的请求体",requestURI,e.getMessage(),e);
        return R.error(HttpStatus.BAD_REQUEST,"缺少所需的请求体");
    }

    /**
     * 校验异常：前段以json格式有效
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',校验异常'{}'",requestURI, e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.error(HttpStatus.BAD_REQUEST,message);
    }

    /**
     * 校验异常：表单提交有效，对于以json格式提交将会失效
     */
    @ExceptionHandler(BindException.class)
    public R handleBindException(BindException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',校验异常'{}'",requestURI,e.getMessage(), e);
        String message = "";
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message += fieldError.getField() + "：" + fieldError.getDefaultMessage() + "!";
        }
        return R.error(HttpStatus.BAD_REQUEST,message);
    }

    /**
     * 校验异常：参数上加@RequestParam或参数加@NotBlank @NotNull等
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R ConstraintViolationExceptionHandler(ConstraintViolationException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<String> msgList = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msgList.add(cvl.getMessageTemplate());
        }
        LOGGER.error("请求地址'{}',校验异常'{}'", requestURI, e.getMessage(), e);
        return R.error(HttpStatus.BAD_REQUEST,String.join(",",msgList));
    }

    /**
     * SQL异常
     */
    @ExceptionHandler(SQLException.class)
    public R handleSQLException(SQLException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}'，发生SQL异常",requestURI,e);
        return R.error(HttpStatus.ERROR,e.getMessage());
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
