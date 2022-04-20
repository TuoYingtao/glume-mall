package com.glume.glumemall.admin.scheduled.quartz.schedule;

import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import com.glume.glumemall.admin.scheduled.quartz.constants.ScheduleConstants;
import com.glume.glumemall.admin.scheduled.quartz.exception.TaskException;
import com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution;
import com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobExecution;
import org.quartz.*;

/**
 * 定时任务工具类
 * @author tuoyingtao
 * @create 2022-04-20 10:50
 */
public class ScheduleUtils {

    /**
     * 获取 Quartz 任务类
     *
     * @param scheduleJobEntity
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(ScheduleJobEntity scheduleJobEntity) {
        boolean isConcurrent = ScheduleConstants.TASK_CONCURRENT.equals(scheduleJobEntity.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzJobDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发器对象
     *
     * @param jobId 工作ID
     * @param jobGroup 工作分组
     * @return 触发器对象
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 构建任务工作对象
     * @param jobId 工作ID
     * @param jobGroup 工作分组
     * @return 工作对象
     */
    public static JobKey getJobKey(Long jobId, String jobGroup) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 创建定时任务
     * @param scheduler 调度器
     * @param scheduleJobEntity 定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJobEntity) throws SchedulerException {
        Class<? extends Job> jobClass = getQuartzJobClass(scheduleJobEntity);

        JobKey jobKey = getJobKey(scheduleJobEntity.getJobId(), scheduleJobEntity.getJobGroup());
        // 构建Job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJobEntity.getCronExpression());
        // 定义表达式策略
        cronScheduleBuilder = handlerCronScheduleMisfirePolicy(cronScheduleBuilder,scheduleJobEntity);

        // 按新的cronExpression表达式构建一个新的trigger
        TriggerKey triggerKey = getTriggerKey(scheduleJobEntity.getJobId(), scheduleJobEntity.getJobGroup());
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, scheduleJobEntity);

        // 判断是否存在
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题，先删除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }

        scheduler.scheduleJob(jobDetail, trigger);

        // 暂停任务
        if (scheduleJobEntity.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            scheduler.pauseJob(jobKey);
        }
    }

    /**
     * 设置定时任务策略
     * @param cronScheduleBuilder
     * @param scheduleJobEntity
     * @return
     */
    public static CronScheduleBuilder handlerCronScheduleMisfirePolicy(CronScheduleBuilder cronScheduleBuilder, ScheduleJobEntity scheduleJobEntity) {
        switch (scheduleJobEntity.getMisfirePolicy().toString()) {
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cronScheduleBuilder;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy '" + scheduleJobEntity.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks", TaskException.Code.CONFIG_ERROR);
        }
    }
}
