package com.glume.glumemall.admin.service.impl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.service.MenuService;
import com.glume.glumemall.admin.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.admin.dao.RoleMenuDao;
import com.glume.glumemall.admin.entity.RoleMenuEntity;
import com.glume.glumemall.admin.service.RoleMenuService;


@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenuEntity> implements RoleMenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleMenuServiceImpl.class);

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
     * 获取用户角色菜单列表
     * @param roleId 角色ID
     * @return
     */
    @Override
    public List<RoleMenuEntity> getRoleMenuEntity(Long roleId) {
        QueryWrapper<RoleMenuEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("role_id",roleId);
        return baseMapper.selectList(objectQueryWrapper);
    }

    /**
     * 获取用户角色菜单ID
     * @param roleId
     * @return
     */
    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        QueryWrapper<RoleMenuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",roleId);
        List<RoleMenuEntity> roleMenuEntities = baseMapper.selectList(wrapper);
        List<Long> list = roleMenuEntities.stream().map(RoleMenuEntity::getMenuId).collect(Collectors.toList());
        return list;
    }

    /**
     * 获取角色菜单权限
     * @param roleId 角色ID
     * @return
     */
    @Override
    public List<String> getRoleAuthorityInfo(Long roleId) {
        List<String> authentication = null;
        List<RoleMenuEntity> menus = baseMapper.selectList(new QueryWrapper<RoleMenuEntity>()
                .inSql("menu_id","select menu_id from sys_user_role where role_id = " + roleId));
        if (menus.size() > 0) {
            List<String> menu = menus.stream().map(menuItem -> menuService.getMenuDetail(menuItem.getMenuId()).getPerms())
                    .filter(element -> element != null).collect(Collectors.toList());
            authentication = menu;
        }
        return authentication;
    }

    /**
     * 根据菜单ID删除角色关系
     * @param menu_id 菜单ID
     * @return
     */
    @Override
    public void removeMenuRoleByIds(List<Long> menu_id) {
        Integer row = baseMapper.delete(new QueryWrapper<RoleMenuEntity>().in("menu_id", menu_id));
        if (row == 0) {
            throw new ServiceException("删除失败！");
        }
    }

    /**
     * 根据角色ID删除
     * @param roleIds
     */
    @Override
    public void removeRoleMenuByIds(List<Long> roleIds) {
        boolean role_id = SqlHelper.retBool(baseMapper.delete(new QueryWrapper<RoleMenuEntity>().in("role_id", roleIds)));
        if (!role_id) {
            throw new ServiceException("删除失败");
        }
    }

    /**
     * 批量保存角色菜单ID
     * @param roleId 角色ID
     * @param menuIds 菜单ID
     */
    @Override
    public void saveMyBatch(Long roleId, Long[] menuIds) {
        ArrayList<RoleMenuEntity> roleMenuEntities = new ArrayList<>();
        for (Long menuId : menuIds) {
            roleMenuEntities.add(new RoleMenuEntity(roleId,menuId));
        }
        boolean b = this.saveBatch(roleMenuEntities);
        if (!b) {
            throw new ServiceException("批量保存角色菜单ID失败！");
        }
    }

    /**
     * 更新角色菜单权限
     * @param roleId 角色ID
     * @param menuIds 当前角色拥有权限的ID
     */
    @Override
    public void updateBatchRoleMenu(Long roleId, Long[] menuIds) {
        // 删除当前角色全部权限
        SqlHelper.retBool(baseMapper.delete( new QueryWrapper<RoleMenuEntity>().eq("role_id",roleId)));
        // 添加当前角色全部权限
        if (StringUtils.isNotEmpty(menuIds)) {
            List<RoleMenuEntity> roleMenuEntities = new ArrayList<>();
            for (Long menuId : menuIds) {
                roleMenuEntities.add(new RoleMenuEntity(roleId,menuId));
            }
            this.saveBatch(roleMenuEntities);
        }
    }

    /**
     * 批量查询
     * @param roleIds 角色ID 集合
     * @return 角色菜单关系集合
     */
    @Override
    public List<RoleMenuEntity> getRoleMenuBatch(List<Long> roleIds) {
        return baseMapper.selectList(new QueryWrapper<RoleMenuEntity>().in("role_id",roleIds));
    }
}
