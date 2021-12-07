package com.glume.glumemall.member.service.impl;

import com.glume.glumemall.member.dao.MemberLevelDao;
import com.glume.glumemall.member.entity.MemberLevelEntity;
import com.glume.glumemall.member.exception.MobileExsitException;
import com.glume.glumemall.member.exception.UserNameExsitException;
import com.glume.glumemall.member.vo.MemberRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.member.dao.MemberDao;
import com.glume.glumemall.member.entity.MemberEntity;
import com.glume.glumemall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVo memberRegisterVo) {
        checkMobileUnique(memberRegisterVo.getMobile());
        checkUsernameUnique(memberRegisterVo.getUserName());
        MemberEntity memberEntity = new MemberEntity();
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(levelEntity.getId());
        memberEntity.setMobile(memberRegisterVo.getMobile());
        memberEntity.setUsername(memberRegisterVo.getUserName());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberEntity.setPassword(passwordEncoder.encode(memberRegisterVo.getPassword()));
        baseMapper.insert(memberEntity);
    }

    @Override
    public void checkMobileUnique(String mobile) throws MobileExsitException {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", mobile));
        if (count > 0) {
            throw new MobileExsitException();
        }
    }

    @Override
    public void checkUsernameUnique(String username) throws UserNameExsitException {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count > 0) {
            throw new UserNameExsitException();
        }
    }

}