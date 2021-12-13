package com.glume.glumemall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.member.exception.MobileExsitException;
import com.glume.glumemall.member.exception.UserNameExsitException;
import com.glume.glumemall.member.feign.CouponFeignService;
import com.glume.glumemall.member.vo.MemberLoginVo;
import com.glume.glumemall.member.vo.MemberRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.member.entity.MemberEntity;
import com.glume.glumemall.member.service.MemberService;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;

import javax.annotation.Resource;


/**
 * 会员
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:23:38
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Resource
    private CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        return R.ok().put("member",memberEntity).put("coupons",couponFeignService.memberCoupons().get("coupons"));
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo loginVo){
        MemberEntity memberEntity = memberService.login(loginVo);
        if (StringUtils.isNotNull(memberEntity)) {
            return R.ok().put("data",memberEntity);
        } else {
            return R.error(HttpStatus.BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(), HttpStatus.BizCodeEnum.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVo memberRegisterVo) {
        try {
            memberService.register(memberRegisterVo);
        } catch (MobileExsitException e) {
            return R.error(HttpStatus.BizCodeEnum.MOBILE_EXIST_EXCEPTION.getCode(), HttpStatus.BizCodeEnum.MOBILE_EXIST_EXCEPTION.getMsg());
        } catch (UserNameExsitException e) {
            return R.error(HttpStatus.BizCodeEnum.USER_EXIST_EXCEPTION.getCode(), HttpStatus.BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
