package com.glume.glumemall.admin.scheduled.asyncpool;

import com.glume.common.core.utils.Threads;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 * @author tuoyingtao
 * @create 2022-03-10 16:57
 */
public class AsyncManager {

    public static AsyncManager me = new AsyncManager();
    /**
     * 操作延迟10毫秒
     */
    private final Integer OPERATE_DELAY_TIME = 10;
    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public AsyncManager() {
    }

    public static AsyncManager me() {
        return me;
    }
    /**
     * 执行任务
     * @param timerTask 任务
     */
    public void executor(TimerTask timerTask) {
        executor.schedule(timerTask,OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }
    /**
     * 停止任务线程池
     */
    public void shutdown() {
        Threads.shutdownAndAwaitTermination(executor);
    }
}
