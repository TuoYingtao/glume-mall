package com.glume.glumemall.authserver.feign;

import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tuoyingtao
 * @create 2021-12-06 14:16
 */
@FeignClient("glume-third-party")
public interface ThirdPartFeignService {

    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("mobile") String mobile, @RequestParam("code") String code);
}
