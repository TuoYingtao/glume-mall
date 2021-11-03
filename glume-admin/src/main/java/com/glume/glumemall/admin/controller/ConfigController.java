package com.glume.glumemall.admin.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glume.glumemall.admin.entity.ConfigEntity;
import com.glume.glumemall.admin.service.ConfigService;
import com.glume.glumemall.common.utils.mybatis.PageUtils;
import com.glume.glumemall.common.utils.R;



/**
 * 系统配置信息表
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:34
 */
@RestController
@RequestMapping("admin/config")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = configService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		ConfigEntity config = configService.getById(id);

        return R.ok().put("config", config);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ConfigEntity config){
		configService.save(config);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ConfigEntity config){
		configService.updateById(config);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		configService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
