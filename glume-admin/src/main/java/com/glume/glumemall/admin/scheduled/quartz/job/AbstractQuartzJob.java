package com.glume.glumemall.admin.scheduled.quartz.job;

import com.alibaba.fastjson.JSON;
import com.glume.common.core.utils.SpringUtils;
import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import com.glume.glumemall.admin.entity.ScheduleJobLogEntity;
import com.glume.glumemall.admin.scheduled.quartz.constants.ScheduleConstants;
import com.glume.glumemall.admin.service.ScheduleJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
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
        ScheduleJobEntity jobEntity = JSON.parseObject((String) context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES), ScheduleJobEntity.class);
        BeanUtils.copyProperties(jobEntity,scheduleJobEntity);
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
        ScheduleJobLogEntity jobLogEntity = new ScheduleJobLogEntity();
        jobLogEntity.setJobId(scheduleJobEntity.getJobId());
        jobLogEntity.setStartTime(startTime);
        jobLogEntity.setStopTime(new Date());
        long runTime = jobLogEntity.getStopTime().getTime() - jobLogEntity.getStartTime().getTime();
        jobLogEntity.setTimes(runTime);
        jobLogEntity.setJobMessage(scheduleJobEntity.getJobName() + "-总耗时：" + runTime + "毫秒");
        if (e != null) {
            jobLogEntity.setStatus(Integer.getInteger(ScheduleConstants.FAIL));
            // 获取错误信息
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            String str = stringWriter.toString();
            jobLogEntity.setExceptionInfo(str);
        } else {
            scheduleJobEntity.setStatus(Integer.getInteger(ScheduleConstants.SUCCESS));
        }
        SpringUtils.getBean(ScheduleJobLogService.class).save(jobLogEntity);
    }

    /**
     * 执行方法由子类重写
     * @param context 工作执行上下文对象
     * @param scheduleJobEntity 定时任务
     */
    protected abstract void doExecute(JobExecutionContext context, ScheduleJobEntity scheduleJobEntity) throws Exception;
}
