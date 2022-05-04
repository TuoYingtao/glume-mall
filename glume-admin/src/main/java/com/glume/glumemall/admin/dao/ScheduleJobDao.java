package com.glume.glumemall.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 定时任务
 * @author tuoyingtao
 * @create 2022-04-19 17:57
 */
@Mapper
public interface ScheduleJobDao extends BaseMapper<ScheduleJobEntity> {

    List<String> groupAll();
}
