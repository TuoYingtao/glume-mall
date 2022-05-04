package com.glume.glumemall.admin.scheduled.quartz.job;

import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理 （允许并发执行）
 * @author tuoyingtao
 * @create 2022-04-20 9:52
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, ScheduleJobEntity scheduleJobEntity) throws Exception {
        JobInvokeUtil.invokeMethod(scheduleJobEntity);
    }
}
