package com.glume.glumemall.seckill.feign;

import com.glume.common.core.utils.R;
import com.glume.glumemall.seckill.to.MailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author tuoyingtao
 * @create 2022-03-28 15:07
 */
@FeignClient("glume-third-party")
public interface ThirdPartyFeignService {

    @PostMapping("/send/mail")
    R send(MailTo mailEntity);
}
