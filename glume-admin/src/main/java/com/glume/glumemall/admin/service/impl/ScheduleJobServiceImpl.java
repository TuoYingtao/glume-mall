package com.glume.glumemall.admin.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.dao.ScheduleJobDao;
import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import com.glume.glumemall.admin.scheduled.quartz.constants.ScheduleConstants;
import com.glume.glumemall.admin.scheduled.quartz.schedule.CronUtils;
import com.glume.glumemall.admin.scheduled.quartz.schedule.ScheduleUtils;
import com.glume.glumemall.admin.service.ScheduleJobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务
 * @author tuoyingtao
 * @create 2022-04-19 17:56
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity> implements ScheduleJobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobService.class);

    @Autowired
    Scheduler scheduler;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<ScheduleJobEntity> wrapper = new QueryWrapper<>();
        String jobName = (String) params.get("jobName");
        if (StringUtils.isNotEmpty(jobName)) wrapper.like("job_name",jobName);
        String jobGroup = (String) params.get("jobGroup");
        if (StringUtils.isNotEmpty(jobGroup)) wrapper.eq("job_group",jobGroup);
        String beanName = (String) params.get("beanName");
        if (StringUtils.isNotEmpty(beanName)) wrapper.eq("bean_name",beanName);
        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) wrapper.eq("status",status);

        IPage<ScheduleJobEntity> page = this.page(
                new Query<ScheduleJobEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveJob(ScheduleJobEntity scheduleJobEntity) throws SchedulerException {
        scheduleJobEntity.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        scheduleJobEntity.setCreateTime(new Date());
        int rows = baseMapper.insert(scheduleJobEntity);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, scheduleJobEntity);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateJobById(ScheduleJobEntity scheduleJobEntity) throws SchedulerException {
        ScheduleJobEntity jobEntity = baseMapper.selectById(scheduleJobEntity.getJobId());
        int rows = baseMapper.updateById(scheduleJobEntity);
        if (rows > 0) {
            updateScheduleJob(scheduleJobEntity,jobEntity.getJobGroup());
        }
        return rows;
    }

    /**
     * 更新任务
     *
     * @param scheduleJobEntity 任务对象
     * @param jobGroup 任务组名
     */
    private void updateScheduleJob(ScheduleJobEntity scheduleJobEntity, String jobGroup) throws SchedulerException {
        JobKey jobKey = ScheduleUtils.getJobKey(scheduleJobEntity.getJobId(), jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler,scheduleJobEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeJobByIds(List<Long> jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            ScheduleJobEntity scheduleJobEntity = baseMapper.selectById(jobId);
            deleteJob(scheduleJobEntity);
        }
    }

    /**
     * 删除Job 后，所对应的 trigger 也将会删除
     * @param scheduleJobEntity 任务对象
     */
    private void deleteJob(ScheduleJobEntity scheduleJobEntity) throws SchedulerException {
        Long jobId = scheduleJobEntity.getJobId();
        String jobGroup = scheduleJobEntity.getJobGroup();
        int rows = baseMapper.deleteById(jobId);
        if (rows > 0) {
            JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
            scheduler.deleteJob(jobKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer switchStatus(Long jobId) throws SchedulerException {
        final Integer NORMAL = ScheduleConstants.Status.NORMAL.getValue();
        final Integer PAUSE = ScheduleConstants.Status.PAUSE.getValue();
        ScheduleJobEntity jobEntity = baseMapper.selectById(jobId);
        jobEntity.setStatus(NORMAL.equals(jobEntity.getStatus()) ? PAUSE : NORMAL);
        int rows = baseMapper.updateById(jobEntity);
        if (rows > 0) {
            renewAndPauseJobStatus(jobEntity);
        }
        return rows;
    }

    /**
     * 恢复/暂停 Job
     * @param jobEntity 定时任务对象
     */
    private void renewAndPauseJobStatus(ScheduleJobEntity jobEntity) throws SchedulerException {
        final Integer NORMAL = ScheduleConstants.Status.NORMAL.getValue();
        final Integer PAUSE = ScheduleConstants.Status.PAUSE.getValue();
        JobKey jobKey = ScheduleUtils.getJobKey(jobEntity.getJobId(), jobEntity.getJobGroup());
        if (jobEntity.getStatus().equals(NORMAL)) {
            // 恢复任务
            scheduler.resumeJob(jobKey);
        } else if (jobEntity.getStatus().equals(PAUSE)) {
            // 暂停任务
            scheduler.pauseJob(jobKey);
        }
    }

    @Override
    public void runJob(Long jobId) throws SchedulerException {
        ScheduleJobEntity jobEntity = baseMapper.selectById(jobId);
        JobKey jobKey = ScheduleUtils.getJobKey(jobEntity.getJobId(), jobEntity.getJobGroup());
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(ScheduleConstants.TASK_PROPERTIES, JSON.toJSONString(jobEntity));
        scheduler.triggerJob(jobKey,jobDataMap);
    }

    @Override
    public ScheduleJobEntity getInfoById(Long jobId) {
        ScheduleJobEntity scheduleJobEntity = baseMapper.selectById(jobId);
        String cronExpression = scheduleJobEntity.getCronExpression();
        if (CronExpression.isValidExpression(cronExpression)) {
            Date nextExecution = CronUtils.getNextExecution(cronExpression);
            scheduleJobEntity.setNextValidTime(nextExecution);
        }
        return scheduleJobEntity;
    }
}
