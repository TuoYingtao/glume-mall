package com.glume.glumemall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;
import com.glume.glumemall.product.entity.AttrEntity;
import com.glume.glumemall.product.entity.CategoryEntity;
import com.glume.glumemall.product.service.AttrService;
import com.glume.glumemall.product.service.CategoryService;
import com.glume.glumemall.product.vo.AttrGroupRelationVo;
import com.glume.glumemall.product.vo.AttrGroupWithAttrVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.product.entity.AttrGroupEntity;
import com.glume.glumemall.product.service.AttrGroupService;



/**
 * 属性分组
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 14:25:13
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    AttrService attrService;

    /**
     * 列表
     */
    @GetMapping("/list/{catelogId}")
    @ApiOperation(value = "属性分组列表")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        HashMap<String, Object> data = new HashMap<>();
        List<CategoryEntity> list = SpringUtils.getBean(CategoryService.class).categoryPath(catelogId);
        data.put("categoryPath",list);
        data.put("page",page);
        return R.ok().put("data", data);
    }

    /**
     * 获取分组关联数据
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> entityList = attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data", entityList);
    }

    /**
     * 获取分组没有关联数据
     */
    @GetMapping("/{attrGroupId}/notattr/relation")
    public R notAttrRelation(@RequestParam Map<String,Object> params,
                             @PathVariable("attrGroupId") Long attrGroupId) {
        PageUtils notRelationAttr = attrService.getNotRelationAttr(params, attrGroupId);
        return R.ok().put("data", notRelationAttr);
    }

    /**
     * 添加属性分组关系
     */
    @PostMapping("/add/attr/relation")
    public R addAttrGroupRelation(Long[] attrId,Long attrGroupId) {
        attrService.saveAttrGroupRelationBatch(attrId,attrGroupId);
        return R.ok("添加属性分组关系成功！");
    }

    /**
     * 删除分组关联记录
     */
    @DeleteMapping("/attr/relation/delete")
    public R deleteRelation(AttrGroupRelationVo[] agv) {
        attrService.deleteRelation(agv);
        return R.ok("删除成功！");
    }

    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    @ApiOperation(value = "查询属性分组详情")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<CategoryEntity> list = SpringUtils.getBean(CategoryService.class).categoryPath(attrGroup.getCatelogId());
        attrGroup.setCategoryPath(list);
        return R.ok().put("data", attrGroup);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存属性分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attrGroupName",value = "组名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "sort",value = "排序",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "descript",value = "描述",required = true,dataType = "String"),
            @ApiImplicitParam(name = "icon",value = "组图标",required = true,dataType = "String"),
            @ApiImplicitParam(name = "catelogId",value = "所属分类id",required = true,dataType = "Long"),
    })
    public R save(@Validated(AddGroup.class) AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok("保存成功！");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改属性分组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attrGroupId",value = "分组id",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "attrGroupName",value = "组名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "sort",value = "排序",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "descript",value = "描述",required = true,dataType = "String"),
            @ApiImplicitParam(name = "icon",value = "组图标",required = true,dataType = "String"),
            @ApiImplicitParam(name = "catelogId",value = "所属分类id",required = true,dataType = "Long"),
    })
    public R update(@Validated(UpdateGroup.class) AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);
        return R.ok("更新成功！");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除属性分组信息")
    @ApiImplicitParam(name = "attrGroupId",value = "分组id",required = true,dataType = "Long")
    public R delete(@Validated(IDGroup.class) Long[] attrGroupIds){
		attrGroupService.removeAttrByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }

    /**
     * 获取分类下的所有分组以及它的属性
     */
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrVo> attrGroupWithAttrsBy = attrGroupService.getAttrGroupWithAttrsBy(catelogId);
        return R.ok().put("data",attrGroupWithAttrsBy);
    }
}
