package com.glume.glumemall.admin.scheduled.quartz.job;

import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import com.glume.glumemall.admin.scheduled.quartz.constants.ScheduleConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 抽象 Quartz 调用
 * @author tuoyingtao
 * @create 2022-04-19 17:08
 */
public abstract class AbstractQuartzJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractQuartzJob.class);

    private static final ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    /**
     * @param context 工作执行上下文对象
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        BeanUtils.copyProperties(scheduleJobEntity,context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        before(context,scheduleJobEntity);
        try {
            if (!StringUtils.isEmpty(scheduleJobEntity)) {
                doExecute(context,scheduleJobEntity);
            }
        } catch (Exception e) {
            LOGGER.error("系统定时任务执行异常：{}",e);
            after(context,scheduleJobEntity,e);
        }
    }

    /**
     * 执行前
     * @param context 工作执行上下文对象
     * @param scheduleJobEntity 定时任务
     */
    protected void before(JobExecutionContext context, ScheduleJobEntity scheduleJobEntity) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     * @param context 工作执行上下文
     * @param scheduleJobEntity 定时任务
     */
    protected void after(JobExecutionContext context, ScheduleJobEntity scheduleJobEntity, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();
    }

    /**
     * 执行方法由子类重写
     * @param context 工作执行上下文对象
     * @param scheduleJobEntity 定时任务
     */
    protected abstract void doExecute(JobExecutionContext context, ScheduleJobEntity scheduleJobEntity) throws Exception;
}
