package com.glume.glumemall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.product.entity.BrandEntity;
import com.glume.glumemall.product.service.BrandService;



/**
 * 品牌
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 14:25:13
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("品牌列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页数"),
            @ApiImplicitParam(name = "limit",value = "每页多少条")
    })
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{brandId}")
    @ApiOperation(value = "获取品牌信息")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getBrandById(brandId);
        HashMap<String, Object> data = new HashMap<>();
        data.put("brand",brand);
        return R.ok().put("data", data);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存品牌信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "品牌名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "logo",value = "品牌logo地址",required = true,dataType = "String"),
            @ApiImplicitParam(name = "descript",value = "描述",required = true,dataType = "String"),
            @ApiImplicitParam(name = "firstLetter",value = "检索首字母",required = true,dataType = "String"),
            @ApiImplicitParam(name = "sort",value = "排序",dataType = "Integer")
    })
    public R save(@Validated BrandEntity brand){
		brandService.save(brand);

        return R.ok("保存成功！");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改品牌信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "brandId",value = "品牌id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "name",value = "品牌名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "logo",value = "品牌logo地址",required = true,dataType = "String"),
            @ApiImplicitParam(name = "descript",value = "描述",required = true,dataType = "String"),
            @ApiImplicitParam(name = "firstLetter",value = "检索首字母",required = true,dataType = "String"),
            @ApiImplicitParam(name = "sort",value = "排序",dataType = "Integer")
    })
    public R update(@Validated BrandEntity brand){
		brandService.updateById(brand);

        return R.ok("更新成功！");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{brandIds}")
    public R delete(@PathVariable("brandIds") Long[] brandIds){
		brandService.removeBrandByIds(Arrays.asList(brandIds));
        return R.ok("删除成功！");
    }

}
