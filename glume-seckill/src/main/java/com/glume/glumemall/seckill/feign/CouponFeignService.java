package com.glume.glumemall.seckill.feign;

import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 优惠服务远程调用
 * @author tuoyingtao
 * @create 2022-03-01 17:23
 */
@FeignClient("glume-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/lates3DaySession")
    R getLates3DaySession();
}
