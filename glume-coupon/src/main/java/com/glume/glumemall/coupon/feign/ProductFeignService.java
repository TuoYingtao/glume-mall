package com.glume.glumemall.coupon.feign;

import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author TuoYingtao
 * @create 2022-03-18 10:52
 */
@FeignClient("glume-product")
public interface ProductFeignService {

    @RequestMapping("/product/skuinfo/info/list")
    R infoList(@RequestBody Long[] skuId);
}
