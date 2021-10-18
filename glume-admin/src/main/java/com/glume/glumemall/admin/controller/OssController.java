package com.glume.glumemall.admin.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glume.glumemall.admin.entity.OssEntity;
import com.glume.glumemall.admin.service.OssService;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.R;



/**
 * 文件上传
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@RestController
@RequestMapping("admin/oss")
public class OssController {
    @Autowired
    private OssService ossService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = ossService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OssEntity oss = ossService.getById(id);

        return R.ok().put("oss", oss);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OssEntity oss){
		ossService.save(oss);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OssEntity oss){
		ossService.updateById(oss);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		ossService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
