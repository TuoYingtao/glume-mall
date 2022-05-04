package com.glume.glumemall.coupon.config;


import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author TuoYingtao
 * @create 2022-03-17 17:17
 */
@Configuration
public class MyThreadPoolConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyThreadPoolConfig.class);

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new ThreadPoolExecutor(pool.getCorePoolSize(), pool.getMaximumPoolSize(), pool.getKeepAliveTime(), TimeUnit.SECONDS,new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "glumemall.thread")
    public static class ThreadPoolConfigProperties {
        /**
         * 核心线程
         */
        private Integer corePoolSize;
        /**
         * 最大线程
         */
        private Integer maximumPoolSize;
        /**
         * 活动时间
         */
        private Integer keepAliveTime;

        public ThreadPoolConfigProperties() {
            this.corePoolSize = 50;
            this.maximumPoolSize = 200;
            this.keepAliveTime = 10;
        }
    }
}
