package com.glume.glumemall.admin.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.glume.glumemall.admin.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    Integer updateResetPassword(@Param("userId") Long userId, @Param("password") String password);

    IPage<UserEntity> myPageList(IPage<UserEntity> page, @Param(Constants.WRAPPER) QueryWrapper<UserEntity> userEntityQueryWrapper);
}
