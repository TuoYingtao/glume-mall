package com.glume.glumemall.admin.service.impl;

import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.service.RoleMenuService;
import com.glume.glumemall.admin.service.RoleService;
import com.glume.glumemall.admin.util.RedisUtils;
import com.glume.glumemall.common.constant.RedisConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    RoleService roleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    RedisUtils redisUtils;

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
        if (redisUtils.hasKey(RedisConstant.REDIS_AUTHORITY_KEY + userId)) {
            authority = (String) redisUtils.get(RedisConstant.REDIS_AUTHORITY_KEY + userId);
            LOGGER.info("获取Redis角色权限：" + authority);
        } else {
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
                Date date = new Date(System.currentTimeMillis() + (60 * 60 * 24 * 1000));
                redisUtils.set(RedisConstant.REDIS_AUTHORITY_KEY + userId,authority,date.getTime());
                LOGGER.info("获取SQL角色权限：" + authority);
            }
        }
        return authority;
    }

}