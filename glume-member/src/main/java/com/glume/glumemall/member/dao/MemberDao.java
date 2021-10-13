package com.glume.glumemall.member.dao;

import com.glume.glumemall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:23:38
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
