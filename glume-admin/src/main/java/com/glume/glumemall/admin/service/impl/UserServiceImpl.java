package com.glume.glumemall.admin.service.impl;

import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.Query;

import com.glume.glumemall.admin.dao.UserDao;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.service.UserService;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    MenuService menuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取用户信息以及它的菜单
     * @param username
     * @return
     */
    @Override
    public HashMap<String, Object> getByUserInfoAndMenu(String username) {
        QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("username",username);
        UserEntity userEntity = baseMapper.selectOne(objectQueryWrapper);
        List<MenuEntity> menuList = menuService.getMenuList(userEntity.getUserId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("info",userEntity);
        map.put("menus",menuList);
        return map;
    }

    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return
     */
    @Override
    public UserEntity getByUserDetail(String username) {
        QueryWrapper<UserEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("username",username);
        UserEntity userEntity = baseMapper.selectOne(objectQueryWrapper);
        return userEntity;
    }

}