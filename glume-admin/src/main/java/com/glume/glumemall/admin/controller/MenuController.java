package com.glume.glumemall.admin.controller;


import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.mybatis.PageUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.service.MenuService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private MenuService menuService;

    /**
     * 列表
     */
    @GetMapping("/list/{userId}")
    @ApiOperation(value = "获取用户菜单",notes = "")
    @ApiImplicitParam(name = "userId",value = "用户ID",required = true,dataType = "Long")
    public R getMenuDetail(@RequestParam Map<String,Object> params, @PathVariable("userId") Long userId) {
        PageUtils page = menuService.queryPage(params);
        List<MenuEntity> menuList = menuService.getMenuList(userId,false);
        HashMap<String, List<MenuEntity>> map = new HashMap<>();
        map.put("menus",menuList);
        return R.ok().put("code",200)
                .put("data",map);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{menuId}")
    @ApiOperation(value = "获取菜单项详情",notes = "获取菜单项详情")
    @ApiImplicitParam(name = "menuId",value = "菜单ID",required = true,dataType = "Long")
    public R info(@PathVariable("menuId") Long menuId){
		MenuEntity menu = menuService.getById(menuId);
        HashMap<String, Object> data = new HashMap<>();
        data.put("menuInfo",menu);
        return R.ok().put("code",200)
                .put("data", data);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加菜单项",notes = "添加菜单项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId",value = "父菜单ID",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "name",value = "菜单名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "path",value = "菜单地址",required = true,dataType = "String"),
            @ApiImplicitParam(name = "component",value = "菜单路径",required = true,dataType = "String"),
            @ApiImplicitParam(name = "query",value = "路由参数",dataType = "String"),
            @ApiImplicitParam(name = "visible",value = "菜单显示状态",required = true,dataType = "Character"),
            @ApiImplicitParam(name = "status",value = "菜单启用状态",required = true,dataType = "Character"),
            @ApiImplicitParam(name = "perms",value = "授权",dataType = "String"),
            @ApiImplicitParam(name = "menuType",value = "菜单类型",required = true,dataType = "Character"),
            @ApiImplicitParam(name = "icon",value = "菜单图标",required = true,dataType = "String"),
            @ApiImplicitParam(name = "orderNum",value = "排序",dataType = "String"),
            @ApiImplicitParam(name = "remark",value = "备注",dataType = "String"),
    })
    public R save(@Validated MenuEntity menu, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String userName = SpringUtils.getBean(JwtUtils.class).getUserNameFromToken(token);
        menuService.addMenuItem(menu,userName);
        return R.ok().put("code",200)
                .put("msg","添加成功！");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新菜单项",notes = "更新菜单项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId",value = "菜单ID",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "parentId",value = "父菜单ID",required = true,dataType = "Long"),
            @ApiImplicitParam(name = "name",value = "菜单名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "path",value = "菜单地址",required = true,dataType = "String"),
            @ApiImplicitParam(name = "component",value = "菜单路径",required = true,dataType = "String"),
            @ApiImplicitParam(name = "query",value = "路由参数",dataType = "String"),
            @ApiImplicitParam(name = "visible",value = "菜单显示状态",required = true,dataType = "Character"),
            @ApiImplicitParam(name = "status",value = "菜单启用状态",required = true,dataType = "Character"),
            @ApiImplicitParam(name = "perms",value = "授权",dataType = "String"),
            @ApiImplicitParam(name = "menuType",value = "菜单类型",required = true,dataType = "Character"),
            @ApiImplicitParam(name = "icon",value = "菜单图标",required = true,dataType = "String"),
            @ApiImplicitParam(name = "orderNum",value = "排序",dataType = "String"),
            @ApiImplicitParam(name = "remark",value = "备注",dataType = "String"),
    })
    public R update(@Validated MenuEntity menuEntity,HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = SpringUtils.getBean(JwtUtils.class).getUserNameFromToken(token);
        menuService.updateMenuItem(menuEntity,username);
        return R.ok().put("code",200)
                .put("msg","更新成功！");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{menuIds}")
    @ApiOperation(value = "删除菜单项",notes = "删除菜单项，可批量删除")
    public R delete(@PathVariable("menuIds") Long[] menuIds){
        menuService.removeMenuByIds(Arrays.asList(menuIds));
        return R.ok().put("code",200)
                .put("msg","删除成功！");
    }

}
