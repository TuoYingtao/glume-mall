package com.glume.glumemall.seckill.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 定时任务测试
 *  定时任务
 *      1、@EnableScheduling 开启定时任务
 *      2、@Scheduled 开启一个定时任务
 *      3、自动配置类 TaskSchedulingAutoConfiguration
 *  异步任务
 *      1、@EnableAsync 开启异步任务
 *      2、@Async 需要异步执行的方法
 *      3、自动配置类 TaskExecutionAutoConfiguration
 * @author tuoyingtao
 * @create 2022-03-01 16:27
 */
@Component
@EnableAsync
@EnableScheduling
public class HelloSchedule {
    private final Logger LOGGER = LoggerFactory.getLogger(HelloSchedule.class);

    /**
     * cron = " 秒 分 时 日 月 周 "
     * 定时任务不应该阻塞。  默认阻塞
     *  1） 让业务运行以异步编排的方式，解决阻塞。
     *  2） 支持定时任务线程池，解决阻塞。(spring.task.scheduling.pool.size=5)
     *  3)  让定时任务异步执行。 (推荐)
     */
    @Async
    @Scheduled(cron = "* * * ? * 2")
    public void hello() throws InterruptedException {
        LOGGER.info("hello...");
        Thread.sleep(3000);
    }
}
