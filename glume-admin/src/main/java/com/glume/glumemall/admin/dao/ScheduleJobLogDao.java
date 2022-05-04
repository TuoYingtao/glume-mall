package com.glume.glumemall.admin.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.glume.glumemall.admin.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 定时任务日志
 * @author tuoyingtao
 * @create 2022-04-22 14:16
 */
@Mapper
public interface ScheduleJobLogDao extends BaseMapper<ScheduleJobLogEntity> {

    @Update("truncate table schedule_job_log")
    void emptyLogDao();

    IPage<ScheduleJobLogEntity> myPageList(IPage<ScheduleJobLogEntity> page, @Param(Constants.WRAPPER) QueryWrapper<ScheduleJobLogEntity> wrapper);

    ScheduleJobLogEntity getInfoById(@Param("logId") Long logId);
}
