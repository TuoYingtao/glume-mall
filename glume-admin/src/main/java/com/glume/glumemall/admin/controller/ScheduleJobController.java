package com.glume.glumemall.admin.controller;

import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.service.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 定时任务
 * @author tuoyingtao
 * @create 2022-04-19 18:11
 */
@RestController
@RequestMapping("admin/schedulejob")
public class ScheduleJobController {

    @Autowired
    ScheduleJobService scheduleJobService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils pageUtils = scheduleJobService.queryPage(params);
        return R.ok().put("data",pageUtils);
    }
}
