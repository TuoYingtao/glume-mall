package com.glume.glumemall.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.glumemall.admin.entity.BlackListEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 黑名单表
 * @author tuoyingtao
 * @create 2022-04-06 15:04
 */
@Mapper
public interface BlackListDao extends BaseMapper<BlackListEntity> {

}
