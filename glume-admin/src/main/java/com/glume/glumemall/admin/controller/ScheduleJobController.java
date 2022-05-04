package com.glume.glumemall.admin.controller;

import com.glume.common.core.constant.Constants;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.ScheduleJobEntity;
import com.glume.glumemall.admin.scheduled.quartz.schedule.CronUtils;
import com.glume.glumemall.admin.service.ScheduleJobService;
import com.glume.glumemall.admin.vo.TaskGroup;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 定时任务
 * @author tuoyingtao
 * @create 2022-04-19 18:11
 */
@RestController
@RequestMapping("admin/schedulejob")
public class ScheduleJobController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobController.class);

    @Autowired
    ScheduleJobService scheduleJobService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils pageUtils = scheduleJobService.queryPage(params);
        return R.ok().put("data",pageUtils);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public R save(ScheduleJobEntity scheduleJobEntity) {
        if (!CronUtils.isCronExpression(scheduleJobEntity.getCronExpression())) {
            return R.error("新增任务'" + scheduleJobEntity.getJobName() + "'失败，Cron 表达式不正确");
        } else if (StringUtils.containsIgnoreCase(scheduleJobEntity.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return R.error("新增任务'" + scheduleJobEntity.getJobName() + "'失败，调用方法字符串不允许'rmi://'调用");
        } else if (StringUtils.containsIgnoreCase(scheduleJobEntity.getInvokeTarget(), Constants.LOOKUP_LDAP)) {
            return R.error("新增任务'" + scheduleJobEntity.getJobName() + "'失败，调用方法字符串不允许'ldap://'调用");
        } else if (StringUtils.containsAnyIgnoreCase(scheduleJobEntity.getInvokeTarget(), new String[]{ Constants.HTTP, Constants.HTTPS })) {
            return R.error("新增任务'" + scheduleJobEntity.getJobName() + "'失败，调用方法字符串不允许'http(s)//'调用");
        }
        try {
            scheduleJobService.saveJob(scheduleJobEntity);
        } catch (SchedulerException e) {
            LOGGER.error("创建定时任务失败：{}",e);
        }
        return R.ok("插入成功！");
    }

    /**
     * 详情
     */
    @GetMapping("/info/{jobId}")
    public R info(@PathVariable("jobId") Long jobId) {
        ScheduleJobEntity scheduleJobEntity = scheduleJobService.getInfoById(jobId);
        return R.ok().put("data",scheduleJobEntity);
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public R update(ScheduleJobEntity scheduleJobEntity) {
        if (!CronUtils.isCronExpression(scheduleJobEntity.getCronExpression())) {
            return R.error("新增任务'" + scheduleJobEntity.getJobName() + "'失败，Cron 表达式不正确");
        } else if (StringUtils.containsIgnoreCase(scheduleJobEntity.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return R.error("新增任务'" + scheduleJobEntity.getJobName() + "'失败，调用方法字符串不允许'rmi://'调用");
        } else if (StringUtils.containsIgnoreCase(scheduleJobEntity.getInvokeTarget(), Constants.LOOKUP_LDAP)) {
            return R.error("新增任务'" + scheduleJobEntity.getJobName() + "'失败，调用方法字符串不允许'ldap://'调用");
        } else if (StringUtils.containsAnyIgnoreCase(scheduleJobEntity.getInvokeTarget(), new String[]{ Constants.HTTP, Constants.HTTPS })) {
            return R.error("新增任务'" + scheduleJobEntity.getJobName() + "'失败，调用方法字符串不允许'http(s)//'调用");
        }
        try {
            scheduleJobService.updateJobById(scheduleJobEntity);
        } catch (SchedulerException e) {
            LOGGER.error("更新定时任务失败：{}",e);
        }
        return R.ok("更新成功！");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{jobIds}")
    public R delete(@PathVariable("jobIds") Long[] jobIds) {
        try {
            scheduleJobService.removeJobByIds(Arrays.asList(jobIds));
        } catch (SchedulerException e) {
            LOGGER.error("删除定时任务失败：{}", e);
        }
        return R.ok("删除成功！");
    }

    /**
     * 修改 Job 状态
     */
    @GetMapping("/status/{jobId}")
    public R switchStatus(@PathVariable("jobId") Long jobId) {
        try {
            scheduleJobService.switchStatus(jobId);
        } catch (SchedulerException e) {
            LOGGER.error("更新定时任务状态失败：{}",e);
        }
        return R.ok("状态更新成功！");
    }

    /**
     * 立即执行一次 Job
     */
    @GetMapping("/run")
    public R runJob(@RequestParam("jobId") Long jobId) {
        try {
            scheduleJobService.runJob(jobId);
        } catch (SchedulerException e) {
            LOGGER.error("执行一次定时任务失败：{}",e);
        }
        return R.ok("执行成功！");
    }

    @GetMapping("/group/all")
    public R groupAll() {
         List<TaskGroup> taskGroup = scheduleJobService.groupAll();
         return R.ok().put("data",taskGroup);
    }

}
