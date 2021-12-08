package com.glume.glumemall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.member.entity.MemberEntity;
import com.glume.glumemall.member.exception.MobileExsitException;
import com.glume.glumemall.member.exception.UserNameExsitException;
import com.glume.glumemall.member.vo.MemberLoginVo;
import com.glume.glumemall.member.vo.MemberRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:23:38
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo memberRegisterVo);

    void checkMobileUnique(String mobile) throws MobileExsitException;

    void checkUsernameUnique(String username) throws UserNameExsitException;

    MemberEntity login(MemberLoginVo loginVo);
}

