package com.glume.glumemall.member.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.member.dao.MemberReceiveAddressDao;
import com.glume.glumemall.member.entity.MemberReceiveAddressEntity;
import com.glume.glumemall.member.service.MemberReceiveAddressService;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取会员地址
     * @param memberId
     * @return
     */
    @Override
    public List<MemberReceiveAddressEntity> getMemberAddress(Long memberId) {
        return baseMapper.selectList(new QueryWrapper<MemberReceiveAddressEntity>().eq("member_id",memberId));
    }

}