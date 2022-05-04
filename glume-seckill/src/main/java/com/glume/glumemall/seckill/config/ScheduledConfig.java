package com.glume.glumemall.seckill.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时任务配置类
 * @author tuoyingtao
 * @create 2022-03-01 16:59
 */
@Configuration
@EnableAsync
@EnableScheduling
public class ScheduledConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledConfig.class);

    @Bean(name = "MyThreadPoolTaskExecutor")
    public SchedulingTaskExecutor schedulingTaskExecutor(SchedulerTaskProperties pool) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix(pool.threadNamePrefix);
        threadPoolTaskExecutor.setCorePoolSize(pool.corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(pool.getMaximumPoolSize());
        threadPoolTaskExecutor.setAwaitTerminationSeconds(pool.awaitTerminationSeconds);
        threadPoolTaskExecutor.setKeepAliveSeconds(pool.keepAliveTime);
        threadPoolTaskExecutor.setQueueCapacity(100000);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "glumemall.scheduler.pool")
    public static class SchedulerTaskProperties {
        /**
         * 线程前缀
         */
        private String threadNamePrefix;
        /**
         * 定时任务线程
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
        /**
         * 允许任务等待的时长
         */
        private Integer awaitTerminationSeconds;
        /**
         * 调度器shutdown被调用时,等待当前被调度的任务完成
         */
        private Boolean waitTasks;

        public SchedulerTaskProperties() {
            this.threadNamePrefix = "seckill-scheduler-";
            this.corePoolSize = 5;
            this.maximumPoolSize = 20;
            this.keepAliveTime = 10;
            this.awaitTerminationSeconds = 60;
            this.waitTasks = true;
        }
    }

}
