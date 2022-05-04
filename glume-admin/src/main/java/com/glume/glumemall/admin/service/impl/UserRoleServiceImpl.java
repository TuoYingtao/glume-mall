package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.glume.common.core.constant.RedisConstant;
import com.glume.common.core.utils.RedisUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.entity.RoleEntity;
import com.glume.glumemall.admin.service.RoleMenuService;
import com.glume.glumemall.admin.service.RoleService;
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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserRoleEntity> page = this.page(
                new Query<UserRoleEntity>().getPage(params),
                new QueryWrapper<UserRoleEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取用户角色
     * @param userId
     * @return
     */
    @Override
    public List<UserRoleEntity> getUserRoleId(Long userId) {
        QueryWrapper<UserRoleEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("user_id",userId);
        return baseMapper.selectList(objectQueryWrapper);
    }


    /**
     * 获取用户角色菜单权限
     * @param userId 用户ID
     * @return
     */
    @Override
    public String getUserAuthorityInfo(Long userId) {
        String authority = "";
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        if (redisUtils.hasKey(RedisConstant.REDIS_AUTHORITY_KEY + userId)) {
            authority = (String) redisUtils.get(RedisConstant.REDIS_AUTHORITY_KEY + userId);
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
                long outTime = new Date(System.currentTimeMillis() + (60 * 60 * 24)).getTime() / 1000;
                redisUtils.set(RedisConstant.REDIS_AUTHORITY_KEY + userId,authority,outTime);
            }
        }
        return authority;
    }

    @Override
    public UserRoleEntity getUserById(Long userId) {
        Wrapper<UserRoleEntity> wrapper = new QueryWrapper<UserRoleEntity>().eq("user_id", userId);
        return baseMapper.selectOne(wrapper);
    }

}
