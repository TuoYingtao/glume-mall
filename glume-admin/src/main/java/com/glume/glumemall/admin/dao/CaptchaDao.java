package com.glume.glumemall.admin.dao;

import com.glume.glumemall.admin.entity.CaptchaEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统验证码
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:34
 */
@Mapper
public interface CaptchaDao extends BaseMapper<CaptchaEntity> {
	
}
