package com.glume.glumemall.seckill.feign;

import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author tuoyingtao
 * @create 2022-03-31 11:23
 */
@FeignClient("glume-member")
public interface MemberFeignService {

    @GetMapping("/member/member/info/{id}")
    R info(@PathVariable("id") Long id);
}
