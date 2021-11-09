package com.glume.glumemall.admin.service.impl;

import com.glume.common.core.constant.Constants;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.DateUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.entity.RoleMenuEntity;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import com.glume.glumemall.admin.service.RoleMenuService;
import com.glume.glumemall.admin.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.admin.dao.MenuDao;
import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.service.MenuService;

import javax.validation.constraints.NotNull;


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
     * 添加菜单项
     * @param menuEntity
     */
    @Override
    public void addMenuItem(MenuEntity menuEntity,String username) {
        menuEntity.setCreateTime(new Date(DateUtils.getSysDateTime()));
        menuEntity.setCreateBy(username);
        Integer row = baseMapper.insert(menuEntity);
        // 没添加一个菜单，都给超级管理员加上
        SpringUtils.getBean(RoleMenuService.class).save(new RoleMenuEntity(2001L,menuEntity.getMenuId()));
        if (row == 0) {
            throw new ServiceException("添加失败！");
        }
    }

    /**
     * 更新菜单项
     * @param menuEntity 菜单实体类
     * @param username 更新者用户名
     */
    @Override
    public void updateMenuItem(MenuEntity menuEntity, String username) {
        menuEntity.setUpdateTime(new Date(DateUtils.getSysDateTime()));
        menuEntity.setUpdateBy(username);
        Integer row = baseMapper.updateById(menuEntity);
        if (row == 0) {
            throw new ServiceException("更新失败！");
        }
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        Integer row = baseMapper.deleteBatchIds(asList);
        // TODO 1.检测当前删除的菜单信息，是否被别的地方引用
        SpringUtils.getBean(RoleMenuService.class).removeMenuRoleByIds(asList);
        if (row == 0) {
            throw new ServiceException("删除失败！");
        }
    }

    /**
     * 获取用户菜单
     * @param userId
     * @return
     */
    @Override
    public List<MenuEntity> getMenuList(Long userId, @NotNull Boolean specific) {
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

            // 判读是否过滤按钮项
            if (specific) {
                List<MenuEntity> list = menuList.stream().filter(menuEntity -> menuEntity.getMenuType().compareTo(Constants.MenuType.BUTTON.getValue()) == 0 ? false : true ).collect(Collectors.toList());
                return handlerMenus(list);
            } else {
                return handlerMenus(menuList);
            }
        } else {
            return null;
        }
    }

    /** 拼接数据结构 */
    private List<MenuEntity> handlerMenus(List<MenuEntity> menuList) {
        return menuList.stream().filter(menuEntity -> menuEntity.getParentId() == 0)
                .map(menuEntity -> {
                    menuEntity.setChildren(getChildrens(menuEntity, menuList));
                    return menuEntity;
                }).distinct().sorted((menu1, menu2) -> menu1.getOrderNum() - menu2.getOrderNum()).collect(Collectors.toList());
    }

    private List<MenuEntity> getChildrens(MenuEntity root,List<MenuEntity> all) {

        return all.stream().filter(menuEntity -> menuEntity.getParentId() == root.getMenuId())
                .map(menuEntity -> {
                    menuEntity.setChildren(getChildrens(menuEntity,all));
                    return menuEntity;
                }).distinct().sorted((menu1,menu2) -> menu1.getOrderNum() - menu2.getOrderNum()).collect(Collectors.toList());
    }

}