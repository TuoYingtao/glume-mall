package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.dao.ScheduleJobLogDao;
import com.glume.glumemall.admin.entity.ScheduleJobLogEntity;
import com.glume.glumemall.admin.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 定时任务日志
 * @author tuoyingtao
 * @create 2022-04-22 14:13
 */
@Service
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ScheduleJobLogEntity> page = this.page(new Query<ScheduleJobLogEntity>().getPage(params),
                new QueryWrapper<ScheduleJobLogEntity>());
        return new PageUtils(page);
    }

}
