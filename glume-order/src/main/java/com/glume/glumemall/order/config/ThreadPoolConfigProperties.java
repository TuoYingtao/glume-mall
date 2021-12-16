package com.glume.glumemall.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池配置实体
 * @author tuoyingtao
 * @create 2021-12-04 9:52
 */
@ConfigurationProperties(prefix = "glumemall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
    /**
     * 核心线程数
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
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
