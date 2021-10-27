package com.glume.glumemall.admin.service.impl;

import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.exception.servlet.ServiceException;
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

import javax.validation.constraints.NotNull;


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
        objectQueryWrapper.select("user_id","username","email","mobile","status","create_time")
                .eq("username",username);
        UserEntity userEntity = baseMapper.selectOne(objectQueryWrapper);
        List<MenuEntity> menuList = menuService.getMenuList(userEntity.getUserId(),true);
        HashMap<String, Object> map = new HashMap<>();
        map.put("info",userEntity);
        map.put("menus",menuList);
        return map;
    }

    /**
     * Security 通过用户名获取用户信息
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

    /**
     * 修改用户信息
     * @param userEntity
     * @return
     */
    @Override
    public void updateUserDetail(@NotNull UserEntity userEntity) {
        Integer row = baseMapper.updateById(userEntity);
        if (row == 0) {
            throw new ServiceException("更新失败！");
        }
    }

}