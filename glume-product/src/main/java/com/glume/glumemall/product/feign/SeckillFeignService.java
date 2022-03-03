package com.glume.glumemall.product.feign;

import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 秒杀远程调用
 * @author tuoyingtao
 * @create 2022-03-03 11:28
 */
@FeignClient("glume-seckill")
public interface SeckillFeignService {

    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId") Long skuId);
}
