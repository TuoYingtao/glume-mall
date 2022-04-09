package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.constant.Constants;
import com.glume.common.core.constant.RedisConstant;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.DateUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.dao.MenuDao;
import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.entity.RoleMenuEntity;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import com.glume.glumemall.admin.service.MenuService;
import com.glume.glumemall.admin.service.RoleMenuService;
import com.glume.glumemall.admin.service.RoleService;
import com.glume.glumemall.admin.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    RoleService roleService;

    @Autowired
    RedisTemplate redisTemplate;

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
    @Transactional
    public void addMenuItem(MenuEntity menuEntity,String username) {
        menuEntity.setCreateTime(new Date(DateUtils.getSysDateTime()));
        menuEntity.setCreateBy(username);
        Integer row = baseMapper.insert(menuEntity);
        if (row == 0) throw new ServiceException("添加失败！");
        RoleEntity roleEntity = roleService.getRoleDetail("admin");
        if (StringUtils.isNotNull(roleEntity)) {
            // 每添加一个菜单，都给超级管理员加上
            roleMenuService.save(new RoleMenuEntity(roleEntity.getRoleId(),menuEntity.getMenuId()));
            BoundHashOperations hashOps = redisTemplate.boundHashOps(RedisConstant.ROLE_MENU);
            if (hashOps.hasKey(roleEntity.getRoleTag())) {
                hashOps.delete(roleEntity.getRoleTag());
            }
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

    /**
     * 删除菜单项
     * @param menuIds 菜单ID
     */
    @Override
    public void removeMenuByIds(List<Long> menuIds) {
        // TODO 1.检测当前删除的菜单信息，是否被别的地方引用
        // 存储需要删除的菜单
        List<MenuEntity> delMenuList = new ArrayList<>();
        // 获取所有菜单
        List<MenuEntity> menuEntities = baseMapper.selectList(new QueryWrapper<MenuEntity>());
        // 遍历需要删除的菜单ID
        for (Long menuId : menuIds) {
            // 通过菜单ID查找出它的所有子菜单
            List<MenuEntity> collect = menuEntities.stream().filter(menuEntity -> menuId.equals(menuEntity.getMenuId())).map(menuEntity -> {
                delMenuList.addAll(handlerMenuId(menuEntity.getMenuId(), menuEntities, delMenuList));
                return menuEntity;
            }).distinct().collect(Collectors.toList());
            delMenuList.addAll(collect);
        }
        List<Long> collect = delMenuList.stream().map(MenuEntity::getMenuId).distinct().collect(Collectors.toList());
        // 批量删除菜单
        Integer row = baseMapper.deleteBatchIds(collect);
        // 批量删除菜单与角色的对应关系
        SpringUtils.getBean(RoleMenuService.class).removeMenuRoleByIds(collect);
        if (row == 0) {
            throw new ServiceException("删除失败！");
        }
    }

    /**
     * 查询子菜单的处理方法
     * @param menuId 父菜单ID
     * @param menuEntities 所有菜单集合
     * @param delMenuList 菜单容器
     * @return
     */
    private List<MenuEntity> handlerMenuId(Long menuId,List<MenuEntity> menuEntities,List<MenuEntity> delMenuList) {
        List<MenuEntity> collect = menuEntities.stream().filter(menuEntity -> menuId.equals(menuEntity.getParentId())).map(menuEntity -> {
            delMenuList.addAll(handlerMenuId(menuEntity.getMenuId(), menuEntities,delMenuList));
            return menuEntity;
        }).distinct().collect(Collectors.toList());
        return collect;
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
