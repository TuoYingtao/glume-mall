package com.glume.glumemall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.coupon.entity.SeckillPromotionEntity;
import com.glume.glumemall.coupon.service.SeckillPromotionService;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * 秒杀活动
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:13:53
 */
@RestController
@RequestMapping("coupon/seckillpromotion")
public class SeckillPromotionController {
    @Autowired
    private SeckillPromotionService seckillPromotionService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillPromotionService.queryPage(params);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SeckillPromotionEntity seckillPromotion = seckillPromotionService.getById(id);

        return R.ok().put("data", seckillPromotion);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody SeckillPromotionEntity seckillPromotion){
		seckillPromotionService.saveSeckillPromotion(seckillPromotion);
        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody SeckillPromotionEntity seckillPromotion){
		seckillPromotionService.updateById(seckillPromotion);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long[] ids){
		seckillPromotionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
