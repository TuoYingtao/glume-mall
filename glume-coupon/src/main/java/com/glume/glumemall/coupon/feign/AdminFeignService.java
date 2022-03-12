package com.glume.glumemall.coupon.feign;

import com.glume.common.core.utils.R;
import com.glume.glumemall.coupon.feign.failBackFactory.AdminFeignServiceFaillBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tuoyingtao
 * @create 2022-03-12 16:50
 */
@FeignClient(value = "glume-admin",fallbackFactory = AdminFeignServiceFaillBackFactory.class)
public interface AdminFeignService {

    @GetMapping("/admin/user/byUserDetail")
    public R getByUserDetail(@RequestParam("username") String username);
}
