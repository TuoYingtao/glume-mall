package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.entity.RoleMenuEntity;
import com.glume.glumemall.admin.entity.TreeSelectEntity;
import com.glume.glumemall.admin.service.MenuService;
import com.glume.glumemall.admin.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.admin.dao.RoleDao;
import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.service.RoleService;


@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleEntity> page = this.page(
                new Query<RoleEntity>().getPage(params),
                new QueryWrapper<RoleEntity>()
        );

        return new PageUtils(page);
    }

    /** 获取角色信息 */
    @Override
    public RoleEntity getRoleDetail(Long role_id) {
        RoleEntity roleEntity = baseMapper.selectById(role_id);
        return roleEntity;
    }

    /** 获取角色详情 */
    @Override
    public HashMap<String,Object> getInfoById(Long roleId) {
        RoleEntity roleEntity = baseMapper.selectOne(new QueryWrapper<RoleEntity>().eq("role_id", roleId));
        if (!StringUtils.isNotNull(roleEntity)) {
            throw new ServiceException("没有ID为" + roleId + "的角色");
        }
        List<Long> roleMenuIds = SpringUtils.getBean(RoleMenuService.class).getRoleMenuIds(roleId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("info",roleEntity);
        hashMap.put("menuIds",roleMenuIds);
        return hashMap;
    }

    /** 更新角色详情 */
    @Override
    public void updateRoleById(RoleEntity role) {
        boolean bool = SqlHelper.retBool(baseMapper.updateById(role));
        if (!bool) {
            throw new ServiceException("更新角色信息失败！");
        }
        Long[] menuIds = StringUtils.isEmpty(role.getMenuIds()) ? null : role.getMenuIds();
        SpringUtils.getBean(RoleMenuService.class).updateBatchRoleMenu(role.getRoleId(),menuIds);
    }

    /** 删除角色信息 */
    @Override
    public void removeRoleByIds(List<Long> roleIds) {
        boolean bool = SqlHelper.retBool(baseMapper.deleteBatchIds(roleIds));
        SpringUtils.getBean(RoleMenuService.class).removeRoleMenuByIds(roleIds);
        if (!bool) {
            throw new ServiceException("删除角色失败");
        }
    }

    /** 保存角色信息 */
    @Override
    public void saveMyRole(RoleEntity roleEntity) {
        // 验证当前角色是否存在
        verifyRoleNameSole(roleEntity.getRoleName());
        //验证当前角色标签是否存在
        verifyRoleTagSole(roleEntity.getRoleTag());
        Integer row = baseMapper.insert(roleEntity);
        if (row == 0) {
            throw new ServiceException(HttpStatus.ERROR,"角色保存失败！");
        }
        if (StringUtils.isNotEmpty(roleEntity.getMenuIds())) {
            SpringUtils.getBean(RoleMenuService.class).saveMyBatch(roleEntity.getRoleId(),roleEntity.getMenuIds());
        }
    }

    private void verifyRoleTagSole(String roleTag) {
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("role_tag",roleTag);
        RoleEntity roleEntity = baseMapper.selectOne(wrapper);
        if (StringUtils.isNotNull(roleEntity)) {
            throw new ServiceException(HttpStatus.ERROR,"角色标签：<" + roleTag + "> 已存在！");
        }
    }

    private void verifyRoleNameSole(String roleName) {
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name",roleName);
        RoleEntity roleEntity = baseMapper.selectOne(wrapper);
        if (StringUtils.isNotNull(roleEntity)){
            throw new ServiceException(HttpStatus.ERROR,"角色名称：<" + roleName + "> 已存在！");
        };
    }



    /** 角色下拉菜单权限列表 */
    @Override
    public HashMap<String, Object> menuTreeService(Long roleId) {
        HashMap<String, Object> hashMap = new HashMap<>();

        // 判断角色ID是否为空
        if (StringUtils.isNotNull(roleId)) {
            // 获取当前角色权限菜单ID
            List<RoleMenuEntity> roleMenuEntity = SpringUtils.getBean(RoleMenuService.class).getRoleMenuEntity(roleId);
            List<Long> ids = roleMenuEntity.stream().map(item -> item.getMenuId()).collect(Collectors.toList());
            hashMap.put("ids",ids);
        }
        // 获取菜单列表
        List<MenuEntity> menuEntities =
                SpringUtils.getBean(MenuService.class).getBaseMapper().selectList(new QueryWrapper<>());
        List<MenuEntity> menuEntityList = menuEntities.stream().filter(menuEntity -> menuEntity.getParentId() == 0).map(item -> {
            item.setChildren(handlerChildrens(item, menuEntities));
            return item;
        }).distinct().sorted((num1, num2) -> num1.getOrderNum() - num2.getOrderNum()).collect(Collectors.toList());

        // 构建前端所需要下拉树结构
        List<TreeSelectEntity> treeSelectEntityList = menuEntityList.stream().map(TreeSelectEntity::new).collect(Collectors.toList());
        hashMap.put("menus",treeSelectEntityList);
        return hashMap;
    }

    private List<MenuEntity> handlerChildrens(MenuEntity root,List<MenuEntity> all) {
        List<MenuEntity> menuEntityList = all.stream().filter(menuEntity -> root.getMenuId() == menuEntity.getParentId()).map(item -> {
            item.setChildren(handlerChildrens(item, all));
            return item;
        }).distinct().sorted((num1, num2) -> num1.getOrderNum() - num2.getOrderNum()).collect(Collectors.toList());
        return menuEntityList;
    }

}