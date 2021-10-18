package com.glume.glumemall.admin.dao;

import com.glume.glumemall.admin.entity.UserRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与角色对应关系
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@Mapper
public interface UserRoleDao extends BaseMapper<UserRoleEntity> {
	
}
