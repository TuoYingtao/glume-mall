package com.glume.glumemall.seckill.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * sentinel 拦截器
 * @author tuoyingtao
 * @create 2022-03-04 10:41
 */
@Configuration
public class SeckillSentinelConfig {

    @PostConstruct
    private void SeckillSentinelInterceptor() {
        WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler() {
            @Override
            public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
                R error = R.error(HttpStatus.BizCodeEnum.TO_MANY_REQUEST.getCode(), HttpStatus.BizCodeEnum.TO_MANY_REQUEST.getMsg());
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json;");
                httpServletResponse.getWriter().write(JSON.toJSONString(error));
            }
        });
    }
}
