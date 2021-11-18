package com.glume.glumemall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.member.entity.MemberLevelEntity;
import com.glume.glumemall.member.service.MemberLevelService;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;



/**
 * 会员等级
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:23:38
 */
@RestController
@RequestMapping("member/memberlevel")
public class MemberLevelController {
    @Autowired
    private MemberLevelService memberLevelService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberLevelService.queryPage(params);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberLevelEntity memberLevel = memberLevelService.getById(id);

        return R.ok().put("data", memberLevel);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@Validated(AddGroup.class) MemberLevelEntity memberLevel){
		memberLevelService.save(memberLevel);

        return R.ok("会员等级保存成功！");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@Validated(UpdateGroup.class) MemberLevelEntity memberLevel){
		memberLevelService.updateById(memberLevel);

        return R.ok("会员等级修改成功！");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@Validated(IDGroup.class) Long[] ids){
		memberLevelService.removeByIds(Arrays.asList(ids));

        return R.ok("会员等级删除成功！");
    }

}
