package com.glume.glumemall.admin.dao;

import com.glume.glumemall.admin.entity.UserTokenEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@Mapper
public interface UserTokenDao extends BaseMapper<UserTokenEntity> {
	
}
