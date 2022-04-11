package com.glume.glumemall.admin.dao;

import com.glume.glumemall.admin.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {

    List<RoleEntity> getRoleByIdBatchList(@Param("ids") List<Long> ids);
}
