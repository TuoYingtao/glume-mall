package com.glume.glumemall.product.controller;

import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.product.entity.CategoryBrandRelationEntity;
import com.glume.glumemall.product.service.CategoryBrandRelationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    @GetMapping("/list")
    @ApiOperation(value = "品牌与分类关系列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@Validated CategoryBrandRelationEntity categorybrandrelation){
        categoryBrandRelationService.save(categorybrandrelation);
        return R.ok("保存成功！");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@Validated(IDGroup.class) Long[] ids){
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
