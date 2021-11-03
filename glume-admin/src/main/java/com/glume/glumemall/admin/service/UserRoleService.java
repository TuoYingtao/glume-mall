package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.UserRoleEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户与角色对应关系
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<UserRoleEntity> getUserRoleId(Long userId);

    String getUserAuthorityInfo(Long userId);
}

