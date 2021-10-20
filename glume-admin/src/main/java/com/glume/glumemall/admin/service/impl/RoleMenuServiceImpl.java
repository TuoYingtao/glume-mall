package com.glume.glumemall.admin.service.impl;
import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.service.MenuService;
import com.glume.glumemall.admin.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.Query;

import com.glume.glumemall.admin.dao.RoleMenuDao;
import com.glume.glumemall.admin.entity.RoleMenuEntity;
import com.glume.glumemall.admin.service.RoleMenuService;


@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenuEntity> implements RoleMenuService {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    MenuService menuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleMenuEntity> page = this.page(
                new Query<RoleMenuEntity>().getPage(params),
                new QueryWrapper<RoleMenuEntity>()
        );
        return new PageUtils(page);
    }

    /**
     * 获取角色菜单权限
     * @param roleId 角色ID
     * @return
     */
    @Override
    public Stream<String> getRoleAuthorityInfo(Long roleId) {
        Stream<String> authentication = null;
        List<RoleMenuEntity> menus = baseMapper.selectList(new QueryWrapper<RoleMenuEntity>()
                .inSql("menu_id","select menu_id from sys_user_role where role_id = " + roleId));
        if (menus.size() > 0) {
            Stream<String> menu = menus.stream().map(menuItem -> menuService.getMenuDetail(menuItem.getMenuId()).getPerms()).distinct();
            authentication = menu;
        }
        return authentication;
    }

}