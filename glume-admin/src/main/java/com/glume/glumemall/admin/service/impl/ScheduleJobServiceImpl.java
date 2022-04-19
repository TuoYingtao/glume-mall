package com.glume.glumemall.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.dao.ScheduleJobDao;
import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import com.glume.glumemall.admin.service.ScheduleJobService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 定时任务
 * @author tuoyingtao
 * @create 2022-04-19 17:56
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity> implements ScheduleJobService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ScheduleJobEntity> page = this.page(
                new Query<ScheduleJobEntity>().getPage(params),
                new QueryWrapper<ScheduleJobEntity>());
        return new PageUtils(page);
    }
}
