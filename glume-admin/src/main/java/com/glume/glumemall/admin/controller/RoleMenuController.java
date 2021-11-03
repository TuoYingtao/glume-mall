package com.glume.glumemall.admin.controller;

import java.util.Arrays;
import java.util.Map;

import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glume.glumemall.admin.entity.RoleMenuEntity;
import com.glume.glumemall.admin.service.RoleMenuService;



/**
 * 角色与菜单对应关系
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@RestController
@RequestMapping("admin/rolemenu")
public class RoleMenuController {
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = roleMenuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		RoleMenuEntity roleMenu = roleMenuService.getById(id);

        return R.ok().put("roleMenu", roleMenu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RoleMenuEntity roleMenu){
		roleMenuService.save(roleMenu);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RoleMenuEntity roleMenu){
		roleMenuService.updateById(roleMenu);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		roleMenuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
