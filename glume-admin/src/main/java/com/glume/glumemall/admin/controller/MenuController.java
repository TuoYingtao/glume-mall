package com.glume.glumemall.admin.controller;

import java.util.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.service.MenuService;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.R;



/**
 * 菜单管理
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@RestController
@RequestMapping("admin/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/menu/{userId}")
    @ApiOperation(value = "获取用户菜单",notes = "")
    @ApiImplicitParam(name = "userId",value = "用户ID",required = true,dataType = "Long")
    public R getMenuDetail(@PathVariable("userId") Long userId) {
        List<MenuEntity> menuList = menuService.getMenuList(userId);
        HashMap<String, List<MenuEntity>> map = new HashMap<>();
        map.put("menus",menuList);
        return R.ok().put("code",200)
                .put("data",map);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = menuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{menuId}")
    public R info(@PathVariable("menuId") Long menuId){
		MenuEntity menu = menuService.getById(menuId);

        return R.ok().put("menu", menu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MenuEntity menu){
		menuService.save(menu);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MenuEntity menu){
		menuService.updateById(menu);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] menuIds){
		menuService.removeByIds(Arrays.asList(menuIds));

        return R.ok();
    }

}
