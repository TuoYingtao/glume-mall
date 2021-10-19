package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tuoyingtao
 * @create 2021-10-19 15:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleMenuServiceImpl {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Test
    public void getUserAuthorityInfo() {
        String authority = "";
        // 获取角色
        List<UserRoleEntity> roles = userRoleService.list(new QueryWrapper<UserRoleEntity>()
                .inSql("role_id", "select role_id from sys_user_role where user_id = " + 1001));
        if (roles.size() > 0) {
            System.out.println(roles.size());
            String collect = roles.stream().map(userRoleEntity -> "ROLE_" + userRoleEntity.getRoleId()).collect(Collectors.joining(","));
            authority = collect;
        }
        System.out.println(authority);

        //获取菜单操作

    }
}
