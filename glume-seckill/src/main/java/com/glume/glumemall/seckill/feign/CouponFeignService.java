package com.glume.glumemall.seckill.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 优惠服务远程调用
 * @author tuoyingtao
 * @create 2022-03-01 17:23
 */
@FeignClient("glume-coupon")
public interface CouponFeignService {
}
