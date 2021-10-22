package com.glume.glumemall.admin.service.impl;

import com.glume.glumemall.admin.entity.RoleMenuEntity;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import com.glume.glumemall.admin.service.RoleMenuService;
import com.glume.glumemall.admin.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.Query;

import com.glume.glumemall.admin.dao.MenuDao;
import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.service.MenuService;


@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MenuEntity> page = this.page(
                new Query<MenuEntity>().getPage(params),
                new QueryWrapper<MenuEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取菜单项
     * @param menuId
     * @return
     */
    @Override
    public MenuEntity getMenuDetail(Long menuId) {
        MenuEntity menuEntity = baseMapper.selectById(menuId);
        return menuEntity;
    }

    /**
     * 获取用户菜单
     * @param userId
     * @return
     */
    @Override
    public List<MenuEntity> getMenuList(Long userId) {
        List<MenuEntity> menuList = new ArrayList<>();
        List<RoleMenuEntity> menuIdList = new ArrayList<>();

        List<UserRoleEntity> userRoleId = userRoleService.getUserRoleId(userId);
        if (userRoleId.size() > 0) {

            userRoleId.forEach(userRoleEntity -> {
                menuIdList.addAll(roleMenuService.getRoleMenuEntity(userRoleEntity.getRoleId()));
            });

            menuIdList.forEach(roleMenuEntity -> {
                menuList.add(baseMapper.selectOne(new QueryWrapper<MenuEntity>().eq("menu_id",roleMenuEntity.getMenuId())));
            });

            /* 拼接数据结构 */
            // 过滤按钮项
            List<MenuEntity> list = menuList.stream().filter(menuEntity -> menuEntity.getType() != 2).collect(Collectors.toList());
            return list.stream().filter(menuEntity -> menuEntity.getParentId() == 0)
                    .map(menuEntity -> {
                        menuEntity.setChildren(getChildrens(menuEntity, list));
                        return menuEntity;
                    }).distinct().sorted((menu1, menu2) -> menu1.getOrderNum() - menu2.getOrderNum()).collect(Collectors.toList());

        } else {
            return null;
        }
    }

    private List<MenuEntity> getChildrens(MenuEntity root,List<MenuEntity> all) {

        return all.stream().filter(menuEntity -> menuEntity.getParentId() == root.getMenuId())
                .map(menuEntity -> {
                    menuEntity.setChildren(getChildrens(menuEntity,all));
                    return menuEntity;
                }).distinct().sorted((menu1,menu2) -> menu1.getOrderNum() - menu2.getOrderNum()).collect(Collectors.toList());
    }

}