package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.utils.StringUtils;
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
        QueryWrapper<ScheduleJobLogEntity> wrapper = new QueryWrapper<>();
        String jobId = (String) params.get("jobId");
        if (StringUtils.isNotEmpty(jobId)) wrapper.eq("job_id",jobId);
        String jobMessage = (String) params.get("jobMessage");
        if (StringUtils.isNotEmpty(jobMessage)) wrapper.like("job_message",jobMessage);
        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) wrapper.eq("status",status);
        String timesStart = (String) params.get("timesStart");
        String timesEnd = (String) params.get("timesEnd");
        if (StringUtils.isNotEmpty(timesStart) && StringUtils.isNotEmpty(timesEnd)) wrapper.between("times",timesStart,timesEnd);
        String dateTime = (String) params.get("dateTime");
        if (StringUtils.isNotEmpty(dateTime)) {
            String[] split = dateTime.split(",");
            if (StringUtils.isNotEmpty(split) && split.length > 1) wrapper.between("start_time",split[0],split[1]);
        }
        IPage<ScheduleJobLogEntity> page = this.page(
                new Query<ScheduleJobLogEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public void emptyLog() {
        baseMapper.emptyLogDao();
    }

}
