package com.glume.glumemall.admin.feign;

import com.glume.common.core.utils.R;
import com.glume.glumemall.admin.feign.failBackFactroy.CouponFeignPromotionFailBackFactory;
import com.glume.glumemall.admin.to.SeckillPromotionTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 秒杀活动主题远程调用
 * @author TuoYingtao
 * @create 2022-03-14 13:55
 */
@FeignClient(value = "glume-coupon",fallbackFactory = CouponFeignPromotionFailBackFactory.class)
public interface CouponFeignPromotionService {

    @GetMapping("/coupon/seckillpromotion/list")
    public R list(@RequestParam Map<String, Object> params);

    @PostMapping("/coupon/seckillpromotion/save")
    public R save(SeckillPromotionTo seckillPromotion);

    @GetMapping("/coupon/seckillpromotion/info/{id}")
    public R info(@PathVariable("id") Long id);

    @PutMapping("/coupon/seckillpromotion/update")
    public R update(@RequestBody SeckillPromotionTo seckillPromotion);

    @DeleteMapping("/coupon/seckillpromotion/delete/{id}")
    public R delete(@PathVariable("id") Long[] ids);

}
