package com.glume.glumemall.product.feign;

import com.glume.common.core.to.SkuReductionTo;
import com.glume.common.core.to.SpuBoundsTo;
import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 积分远程服务
 * @author TuoYingtao
 * @create 2021-11-22 21:06
 */

@FeignClient("glume-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
