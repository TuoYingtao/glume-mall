package com.glume.glumemall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;
import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.product.entity.AttrEntity;
import com.glume.glumemall.product.service.AttrService;
import sun.rmi.runtime.Log;


/**
 * 商品属性
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 14:25:13
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    CategoryService categoryService;

    /**
     * 列表
     */
    @GetMapping("/list/{catelogId}")
    @ApiOperation(value = "商品属性列表",notes = "商品属性列表")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrService.queryPage(params, catelogId);
        HashMap<Object, Object> data = new HashMap<>();
        List<CategoryEntity> list = categoryService.categoryPath(catelogId);
        data.put("categoryPath",list);
        data.put("page",page);
        return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrEntity attr){
		attrService.save(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
