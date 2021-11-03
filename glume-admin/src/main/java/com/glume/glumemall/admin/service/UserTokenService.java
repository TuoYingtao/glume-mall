package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.UserTokenEntity;

import java.util.Map;

/**
 * 系统用户Token
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
public interface UserTokenService extends IService<UserTokenEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

