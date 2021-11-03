package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.CaptchaEntity;

import java.util.Map;

/**
 * 系统验证码
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:34
 */
public interface CaptchaService extends IService<CaptchaEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

