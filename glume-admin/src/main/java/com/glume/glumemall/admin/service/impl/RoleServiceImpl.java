package com.glume.glumemall.admin.service.impl;

import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
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
        QueryWrapper<MenuEntity> entityQueryWrapper = new QueryWrapper<>();
        List<MenuEntity> menuEntities =
                SpringUtils.getBean(MenuService.class).getBaseMapper().selectList(entityQueryWrapper);
        List<MenuEntity> menuEntityList = menuEntities.stream().map(item -> {
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