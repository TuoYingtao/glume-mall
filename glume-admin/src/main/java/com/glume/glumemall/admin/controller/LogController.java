package com.glume.glumemall.admin.controller;

import java.util.Arrays;
import java.util.Map;

import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glume.glumemall.admin.entity.LogEntity;
import com.glume.glumemall.admin.service.LogService;



/**
 * 系统日志
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@RestController
@RequestMapping("admin/log")
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = logService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		LogEntity log = logService.getById(id);

        return R.ok().put("log", log);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody LogEntity log){
		logService.save(log);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody LogEntity log){
		logService.updateById(log);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		logService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
