package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/**
 * 定时任务日志
 * @author tuoyingtao
 * @create 2022-04-22 14:10
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

    PageUtils queryPage(Map<String,Object> params);

    void emptyLog();

    ScheduleJobLogEntity getInfoById(Long logId);
}
