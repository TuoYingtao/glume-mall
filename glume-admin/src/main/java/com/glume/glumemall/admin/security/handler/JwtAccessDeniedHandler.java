package com.glume.glumemall.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.ServletUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证权限异常不足
 * @author tuoyingtao
 * @create 2021-10-19 10:40
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) {
        R result = R.error().put("code", HttpStatus.ERROR)
                .put("msg",e.getMessage());
        ServletUtils.renderString(httpServletResponse,HttpStatus.ERROR,JSON.toJSONString(result));
    }
}
