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
     * ???????????????
     * @param menuId
     * @return
     */
    @Override
    public MenuEntity getMenuDetail(Long menuId) {
        MenuEntity menuEntity = baseMapper.selectById(menuId);
        return menuEntity;
    }

    /**
     * ???????????????
     * @param menuEntity
     */
    @Override
    @Transactional
    public void addMenuItem(MenuEntity menuEntity,String username) {
        menuEntity.setCreateTime(new Date(DateUtils.getSysDateTime()));
        menuEntity.setCreateBy(username);
        Integer row = baseMapper.insert(menuEntity);
        if (row == 0) throw new ServiceException("???????????????");
        RoleEntity roleEntity = roleService.getRoleDetail("admin");
        if (StringUtils.isNotNull(roleEntity)) {
            // ???????????????????????????????????????????????????
            roleMenuService.save(new RoleMenuEntity(roleEntity.getRoleId(),menuEntity.getMenuId()));
            BoundHashOperations hashOps = redisTemplate.boundHashOps(RedisConstant.ROLE_MENU);
            if (hashOps.hasKey(roleEntity.getRoleTag())) {
                hashOps.delete(roleEntity.getRoleTag());
            }
        }
    }

    /**
     * ???????????????
     * @param menuEntity ???????????????
     * @param username ??????????????????
     */
    @Override
    public void updateMenuItem(MenuEntity menuEntity, String username) {
        menuEntity.setUpdateTime(new Date(DateUtils.getSysDateTime()));
        menuEntity.setUpdateBy(username);
        Integer row = baseMapper.updateById(menuEntity);
        QueryWrapper<RoleMenuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("menu_id",menuEntity.getMenuId());
        List<Long> roleIDList = roleMenuService.list(wrapper).stream().map(RoleMenuEntity::getRoleId).collect(Collectors.toList());
        if (StringUtils.isNotNull(roleIDList)) {
            List<RoleEntity> roleEntities = roleService.getRoleByIdBatchList(roleIDList);
            if (StringUtils.isNotNull(roleEntities)) {
                BoundHashOperations hashOps = redisTemplate.boundHashOps(RedisConstant.ROLE_MENU);
                List<String> list = roleEntities.stream().filter(roleEntity -> hashOps.hasKey(roleEntity.getRoleTag()))
                        .map(RoleEntity::getRoleTag).collect(Collectors.toList());
                hashOps.delete(String.join(",",list));
            }
        }
        if (row == 0) {
            throw new ServiceException("???????????????");
        }
    }

    /**
     * ???????????????
     * @param menuIds ??????ID
     */
    @Override
    public void removeMenuByIds(List<Long> menuIds) {
        // ???????????????????????????
        List<MenuEntity> delMenuList = new ArrayList<>();
        // ??????????????????
        List<MenuEntity> menuEntities = baseMapper.selectList(new QueryWrapper<MenuEntity>());
        // ???????????????????????????ID
        for (Long menuId : menuIds) {
            // ????????????ID??????????????????????????????
            List<MenuEntity> collect = menuEntities.stream().filter(menuEntity -> menuId.equals(menuEntity.getMenuId())).map(menuEntity -> {
                delMenuList.addAll(handlerMenuId(menuEntity.getMenuId(), menuEntities, delMenuList));
                return menuEntity;
            }).distinct().collect(Collectors.toList());
            delMenuList.addAll(collect);
        }
        List<Long> collect = delMenuList.stream().map(MenuEntity::getMenuId).distinct().collect(Collectors.toList());
        // ??????????????????
        Integer row = baseMapper.deleteBatchIds(collect);
        // ??????????????????????????????????????????
        SpringUtils.getBean(RoleMenuService.class).removeMenuRoleByIds(collect);
        if (row == 0) {
            throw new ServiceException("???????????????");
        }
    }

    /**
     * ??????????????????????????????
     * @param menuId ?????????ID
     * @param menuEntities ??????????????????
     * @param delMenuList ????????????
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
     * ??????????????????
     * @param userId
     * @return
     */
    @Override
    public List<MenuEntity> getMenuList(Long userId, @NotNull Boolean specific) {
        List<MenuEntity> menuList = new ArrayList<>();
        List<RoleMenuEntity> menuIdList = new ArrayList<>();
        // ????????????????????????
        List<UserRoleEntity> userRoleId = userRoleService.getUserRoleId(userId);
        if (userRoleId.size() > 0) {
            // ????????????????????????????????????
            List<Long> roleIds = userRoleId.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
            menuIdList.addAll(roleMenuService.getRoleMenuBatch(roleIds));
            // ??????????????????????????????
            List<Long> menuIds = menuIdList.stream().map(RoleMenuEntity::getMenuId).distinct().collect(Collectors.toList());
            List<MenuEntity> menuEntityList = baseMapper.selectList(new QueryWrapper<MenuEntity>().in("menu_id", menuIds));
            menuList.addAll(menuEntityList);
            // ???????????????????????????
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

    /** ?????????????????? */
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
