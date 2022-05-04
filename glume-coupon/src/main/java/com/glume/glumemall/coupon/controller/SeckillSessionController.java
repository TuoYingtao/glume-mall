package com.glume.glumemall.coupon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.coupon.entity.SeckillSessionEntity;
import com.glume.glumemall.coupon.service.SeckillSessionService;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;

import javax.validation.Valid;


/**
 * 秒杀活动场次
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:13:53
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;

    @GetMapping("/lates3DaySession")
    public R getLates3DaySession() {
        List<SeckillSessionEntity> list = seckillSessionService.getLates3DaySession();

        return R.ok().put("data",list);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@Validated(IDGroup.class) @PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("data", seckillSession);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@Valid SeckillSessionEntity seckillSession){
		seckillSessionService.saveSeckillSession(seckillSession);
        return R.ok("保存成功！");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@Validated({IDGroup.class, UpdateGroup.class}) SeckillSessionEntity seckillSession){
        seckillSession.setStartTime(seckillSession.getDateTime()[0]);
        seckillSession.setEndTime(seckillSession.getDateTime()[1]);
		seckillSessionService.updateById(seckillSession);
        return R.ok("修改成功！");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable Long[] ids){
		seckillSessionService.removeSessionById(Arrays.asList(ids));
        return R.ok("删除成功！");
    }

}
