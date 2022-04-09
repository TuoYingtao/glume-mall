package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.LoginEntity;

import java.util.Map;

/**
 * 在线列表
 * @author tuoyingtao
 * @create 2022-04-06 15:26
 */
public interface LoginService extends IService<LoginEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
