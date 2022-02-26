package com.glume.glumemall.member.feign;

import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 订单远程调用
 * @author tuoyingtao
 * @create 2022-02-26 11:46
 */
@FeignClient("glume-order")
public interface OrderFeignService {

    @PostMapping("/order/order/listWithItem")
    R listWithItem(@RequestBody Map<String, Object> params);
}
