package com.glume.glumemall.admin.service.impl;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import com.glume.glumemall.admin.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleMenuEntity> page = this.page(
                new Query<RoleMenuEntity>().getPage(params),
                new QueryWrapper<RoleMenuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        String authority = "";
        // 获取角色
        List<UserRoleEntity> roles = userRoleService.list(new QueryWrapper<UserRoleEntity>()
                .inSql("role_id", "select role_id from sys_user_role where user_id = " + userId));
        if (roles.size() > 0) {
            String collect = roles.stream().map(userRoleEntity -> "ROLE_" + userRoleEntity.getRoleId()).collect(Collectors.joining(","));
            authority = collect;
        }

        return null;
    }

}