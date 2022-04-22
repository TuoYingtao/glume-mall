package com.glume.glumemall.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.glumemall.admin.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 * @author tuoyingtao
 * @create 2022-04-22 14:16
 */
@Mapper
public interface ScheduleJobLogDao extends BaseMapper<ScheduleJobLogEntity> {
}
