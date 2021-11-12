package com.glume.glumemall.product.controller;

import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.product.entity.CategoryBrandRelationEntity;
import com.glume.glumemall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 品牌&分类关联
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 14:25:13
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CategoryBrandRelationEntity categorybrandrelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categorybrandrelation", categorybrandrelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categorybrandrelation){
        categoryBrandRelationService.save(categorybrandrelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categorybrandrelation){
        categoryBrandRelationService.updateById(categorybrandrelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
