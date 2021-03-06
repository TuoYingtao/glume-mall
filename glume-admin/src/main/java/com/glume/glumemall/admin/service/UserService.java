package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.vo.UserInfoAndMenuVo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
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

    UserInfoAndMenuVo getByUserInfoAndMenu(String username);

    UserEntity getByUserDetail(String username);

    void updateUserDetail(@NotNull UserEntity userEntity);

    Map<String, String> createCaptcha() throws IOException;

    Map<String, Object> info(HttpServletRequest httpServletRequest);

    void resetPassword(Long userId, String password);

    void saveUserInfo(UserEntity user, HttpServletRequest request);
}

