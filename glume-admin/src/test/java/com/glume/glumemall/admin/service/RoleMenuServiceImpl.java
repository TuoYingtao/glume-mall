package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.admin.dao.UserRoleDao;
import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.entity.UserRoleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleMenuServiceImpl.class);

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    RoleService roleService;

    @Test
    public void getUserAuthorityInfo() {
        String authority = "";
        List<String> roleStream = new ArrayList<>();
        List<String> menuStream = new ArrayList<>();
        // 获取角色
        List<UserRoleEntity> roles = baseMapper.selectList(new QueryWrapper<UserRoleEntity>()
                .inSql("role_id", "select role_id from sys_user_role where user_id = " + 1001));
        if (roles.size() > 0) {
            List<String> collect = roles.stream().map(userRoleEntity -> {
                menuStream.addAll(roleMenuService.getRoleAuthorityInfo(userRoleEntity.getRoleId()));
                RoleEntity roleDetail = roleService.getRoleDetail(userRoleEntity.getRoleId());
                return "ROLE_" + roleDetail.getRoleTag();
            }).distinct().collect(Collectors.toList());
            menuStream.addAll(collect);

            authority = menuStream.stream().distinct().collect(Collectors.joining(","));
//            for (UserRoleEntity menu : roles) {
//                menuStream.addAll(roleMenuService.getRoleAuthorityInfo(menu.getRoleId()));
//            }
        }
//        Stream.concat(roleStream.stream(),menuStream.stream()).distinct().collect(Collectors.joining(","));
        LOGGER.info("menuStream：{}", menuStream);
        LOGGER.info("角色权限：" + authority);
    }
}
