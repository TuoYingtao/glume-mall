package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.ScheduleJobEntity;

import java.util.Map;

/**
 * 定时任务
 * @author tuoyingtao
 * @create 2022-04-19 17:54
 */
public interface ScheduleJobService extends IService<ScheduleJobEntity> {

    PageUtils queryPage(Map<String,Object> params);
}
