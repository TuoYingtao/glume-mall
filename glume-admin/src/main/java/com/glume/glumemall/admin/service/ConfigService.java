package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.ConfigEntity;

import java.util.Map;

/**
 * 系统配置信息表
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:34
 */
public interface ConfigService extends IService<ConfigEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

