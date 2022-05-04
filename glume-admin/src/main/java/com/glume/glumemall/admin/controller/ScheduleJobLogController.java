package com.glume.glumemall.admin.controller;

import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.ScheduleJobLogEntity;
import com.glume.glumemall.admin.service.ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 定时任务日志
 * @author tuoyingtao
 * @create 2022-04-29 15:05
 */
@RestController
@RequestMapping("admin/taskLog")
public class ScheduleJobLogController {

    @Autowired
    ScheduleJobLogService scheduleJobLogService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils pageUtils = scheduleJobLogService.queryPage(params);
        return R.ok().put("data",pageUtils);
    }

    @GetMapping("/info/{logId}")
    public R info(@PathVariable("logId") Long logId) {
        ScheduleJobLogEntity jobLogEntity = scheduleJobLogService.getInfoById(logId);
        return R.ok().put("data",jobLogEntity);
    }

    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable("ids") Long[] ids) {
        scheduleJobLogService.removeByIds(Arrays.asList(ids));
        return R.ok("删除成功！");
    }

    @GetMapping("/emptyLog")
    public R emptyLog() {
        scheduleJobLogService.emptyLog();
        return R.ok("已清空所有记录！");
    }
}
