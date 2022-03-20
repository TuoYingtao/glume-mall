package com.glume.glumemall.admin.feign;

import com.glume.common.core.utils.R;
import com.glume.glumemall.admin.to.SeckillPromotionTo;
import com.glume.glumemall.admin.to.SeckillRelationTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 秒杀活动主题远程调用
 * @author TuoYingtao
 * @create 2022-03-14 13:55
 */
@FeignClient(value = "glume-coupon")
public interface CouponFeignService {

    /** 秒杀活动主题 */
    @GetMapping("/coupon/seckillpromotion/list")
    R promotionList(@RequestParam Map<String, Object> params);

    @PostMapping("/coupon/seckillpromotion/save")
    R promotionSave(SeckillPromotionTo seckillPromotion);

    @GetMapping("/coupon/seckillpromotion/info/{id}")
    R promotionInfo(@PathVariable("id") Long id);

    @PutMapping("/coupon/seckillpromotion/update")
    R promotionUpdate(@RequestBody SeckillPromotionTo seckillPromotion);

    @DeleteMapping("/coupon/seckillpromotion/delete/{id}")
    R promotionDelete(@PathVariable("id") Long[] ids);


    /** 秒杀活动品牌关系 */
    @GetMapping("/coupon/seckillskurelation/promotionandsession")
    R promotionAdnSessionList();

    @RequestMapping("/coupon/seckillskurelation/list")
    R skuRelationList(@RequestParam Map<String, Object> params);

    @GetMapping("/coupon/seckillskurelation/info/{id}")
    R skuRelationInfo(@PathVariable("id") Long id);

    @PostMapping("/coupon/seckillskurelation/save")
    R skuRelationSave(@RequestBody SeckillRelationTo seckillRelationTo);

    @PutMapping("/coupon/seckillskurelation/update")
    R skuRelationUpdate(@RequestBody SeckillRelationTo seckillPromotion);

    @DeleteMapping("/coupon/seckillskurelation/delete/{id}")
    R skuRelationDelete(@PathVariable("id") Long[] ids);

}
