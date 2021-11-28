package com.glume.glumemall.ware.feign;

import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author TuoYingtao
 * @create 2021-11-28 18:39
 */
@FeignClient("glume-product")
public interface ProductFeignService {

    @RequestMapping("/api/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
