package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

/**
 * 定时任务
 * @author tuoyingtao
 * @create 2022-04-19 17:54
 */
public interface ScheduleJobService extends IService<ScheduleJobEntity> {

    PageUtils queryPage(Map<String,Object> params);

    Integer saveJob(ScheduleJobEntity scheduleJobEntity) throws SchedulerException;

    Integer updateJobById(ScheduleJobEntity scheduleJobEntity) throws SchedulerException;

    void removeJobByIds(List<Long> jobIds) throws SchedulerException;

    Integer switchStatus(Long jobId) throws SchedulerException;

    void runJob(Long jobId) throws SchedulerException;

    ScheduleJobEntity getInfoById(Long jobId);
}
