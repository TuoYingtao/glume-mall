package com.glume.glumemall.admin.controller.feign;

import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.feign.CouponFeignPromotionService;
import com.glume.glumemall.admin.service.FeignCouponService;
import com.glume.glumemall.admin.to.SeckillPromotionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2022-03-14 13:53
 */
@RestController
@RequestMapping("admin/coupon")
public class FeignCouponPromotionController {

    @Autowired
    FeignCouponService feignCouponService;

    @Autowired
    CouponFeignPromotionService couponFeignPromotionService;

    @GetMapping("/list/seckillPromotion")
    public R listSeckillPromotion(@RequestParam Map<String,Object> params) {
        PageUtils pageUtils = feignCouponService.listSeckillPromotion(params);
        return R.ok().put("data",pageUtils);
    }

    @GetMapping("/info/seckillPromotion/{id}")
    public R infoSeckillPromotion(@Validated(IDGroup.class) @PathVariable("id") Long id) {
        return couponFeignPromotionService.info(id);
    }

    @PutMapping("/update/seckillPromotion")
    public R updateSeckillPromotion(@Validated(UpdateGroup.class) SeckillPromotionTo seckillPromotionTo) {
        R result = feignCouponService.updateSeckillPromotion(seckillPromotionTo);
        return result;
    }

    @PostMapping("/save/seckillPromotion")
    public R saveSeckillPromotion(HttpServletRequest request,@Valid SeckillPromotionTo seckillPromotionTo) {
        R result = feignCouponService.saveSeckillPromotion(request,seckillPromotionTo);
        return result;
    }

    @DeleteMapping("/delete/seckillPromotion/{id}")
    public R deletSeckillPromotion(@Validated(IDGroup.class) @PathVariable("id") Long[] ids) {
        return couponFeignPromotionService.delete(ids);
    }
}
