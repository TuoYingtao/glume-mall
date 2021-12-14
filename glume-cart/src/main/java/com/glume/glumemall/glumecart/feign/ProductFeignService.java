package com.glume.glumemall.glumecart.feign;

import com.glume.common.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author tuoyingtao
 * @create 2021-12-14 14:15
 */
@FeignClient("glume-product")
public interface ProductFeignService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    @GetMapping("/admin/skusaleattrvalue/stringlsit/{skuId}")
    List<String> getSkuSaleAttrValue(@PathVariable("skuId") Long skuId);
}
