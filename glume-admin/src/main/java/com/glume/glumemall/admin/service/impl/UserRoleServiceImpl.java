package com.glume.glumemall.admin.service.impl;

import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.service.RoleMenuService;
import com.glume.glumemall.admin.service.RoleService;
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

import com.glume.glumemall.admin.dao.UserRoleDao;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import com.glume.glumemall.admin.service.UserRoleService;


@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRoleEntity> implements UserRoleService {

    @Autowired
    RoleService roleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserRoleEntity> page = this.page(
                new Query<UserRoleEntity>().getPage(params),
                new QueryWrapper<UserRoleEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取用户角色菜单权限
     * @param userId 用户ID
     * @return
     */
    @Override
    public String getUserAuthorityInfo(Long userId) {
        String authority = "";
        List<String> list = new ArrayList<>();
        // 获取角色
        List<UserRoleEntity> roles = baseMapper.selectList(new QueryWrapper<UserRoleEntity>()
                .inSql("role_id", "select role_id from sys_user_role where user_id = " + userId));
        if (roles.size() > 0) {
            List<String> roleList = roles.stream().map(userRoleEntity -> {
                RoleEntity roleDetail = roleService.getRoleDetail(userRoleEntity.getRoleId());
                list.addAll(roleMenuService.getRoleAuthorityInfo(userRoleEntity.getRoleId()));
                return "ROLE_" + roleDetail.getRoleTag();
            }).distinct().collect(Collectors.toList());
            list.addAll(roleList);
            authority = list.stream().distinct().collect(Collectors.joining(","));
        }
        return authority;
    }

}