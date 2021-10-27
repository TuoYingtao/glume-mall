package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.admin.entity.UserEntity;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统用户
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    HashMap<String,Object> getByUserInfoAndMenu(String username);

    UserEntity getByUserDetail(String username);

    Integer updateUserDetail(@NotNull UserEntity userEntity);
}

