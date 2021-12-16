package com.glume.glumemall.order.feign;

import com.glume.glumemall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author tuoyingtao
 * @create 2021-12-16 11:28
 */
@FeignClient("glume-order")
public interface CartFeignService {

    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();
}
