package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.admin.dao.UserRoleDao;
import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tuoyingtao
 * @create 2021-10-19 15:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleMenuServiceImpl extends ServiceImpl<UserRoleDao, UserRoleEntity> {

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    RoleService roleService;

    @Test
    public void getUserAuthorityInfo() {
        String authority = "";
        // 获取角色
        List<UserRoleEntity> roles = baseMapper.selectList(new QueryWrapper<UserRoleEntity>()
                .inSql("role_id", "select role_id from sys_user_role where user_id = " + 1001));
        if (roles.size() > 0) {
            String authentication = roles.stream().map(userRoleEntity -> {
                RoleEntity roleDetail = roleService.getRoleDetail(userRoleEntity.getRoleId());
                Stream<String> menuAuthentication = roleMenuService.getRoleAuthorityInfo(userRoleEntity.getRoleId());
                return "ROLE_" + roleDetail.getRoleTag() + "," + menuAuthentication;
            }).distinct().collect(Collectors.joining(","));
            authority = authentication;
        }
        System.out.println(authority);
    }
}
