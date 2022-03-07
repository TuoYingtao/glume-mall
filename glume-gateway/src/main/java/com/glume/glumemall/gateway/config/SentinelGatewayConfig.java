package com.glume.glumemall.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson.JSON;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 * @author TuoYingtao
 * @create 2022-03-05 19:19
 */
@Configuration
public class SentinelGatewayConfig {

    @PostConstruct
    public void SentinelGatewayCallbackManager() {
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            // 网关限流了请求，就会调用此回调 Flux、Mono （响应式编程）
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                R error = R.error(HttpStatus.BizCodeEnum.TO_MANY_REQUEST.getCode(), HttpStatus.BizCodeEnum.TO_MANY_REQUEST.getMsg());
                String toJSONString = JSON.toJSONString(error);
                Mono<ServerResponse> serverResponseMono = ServerResponse.ok().body(Mono.just(toJSONString), String.class);
                return serverResponseMono;
            }
        });
    }
}
