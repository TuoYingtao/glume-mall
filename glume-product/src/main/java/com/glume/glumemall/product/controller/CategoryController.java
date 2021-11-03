package com.glume.glumemall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.glume.glumemall.common.utils.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.CategoryService;



/**
 * 商品三级分类
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 14:25:13
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类以及子分类，以树形结构组装起来
     */
    @GetMapping("/list/tree")
    @ApiOperation(value = "商品菜单分类")
    public R list(){
        List<CategoryEntity> entities = categoryService.listWithTree();
        return R.ok().put("code",200)
                .put("data", entities);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{catId}")
    @ApiOperation(value = "获取菜单详情")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);
        HashMap<Object, Object> data = new HashMap<>();
        data.put("category",category);
        return R.ok().put("code",200)
                .put("data", data);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "分类名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "parentCid",value = "父分类id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "catLevel",value = "层级",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "sort",value = "排序",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "icon",value = "图标地址",required = true,dataType = "String"),
            @ApiImplicitParam(name = "productCount",value = "商品数量",required = true,dataType = "Integer")
    })
    public R save(@Validated CategoryEntity category){
		categoryService.save(category);
        return R.ok().put("code",200)
                .put("msg","保存成功");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "catId",value = "菜单ID",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "name",value = "分类名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "parentCid",value = "父分类id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "catLevel",value = "层级",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "sort",value = "排序",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "icon",value = "图标地址",required = true,dataType = "String"),
            @ApiImplicitParam(name = "productCount",value = "商品数量",required = true,dataType = "Integer")
    })
    public R update(@Validated CategoryEntity category){
		categoryService.updateById(category);
        return R.ok().put("code",200)
                .put("msg","修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{catId}")
    @ApiOperation(value = "删除菜单")
    public R delete(@PathVariable("catId") Long[] catIds){
        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok().put("code",200)
                .put("msg","删除成功");
    }

}
