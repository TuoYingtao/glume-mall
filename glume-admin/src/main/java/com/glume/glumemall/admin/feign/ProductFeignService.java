package com.glume.glumemall.admin.feign;

import com.glume.common.core.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author TuoYingtao
 * @create 2022-03-20 13:13
 */
@FeignClient("glume-product")
public interface ProductFeignService {

    @GetMapping("/product/skuinfo/list/all")
    R listAll();

    @GetMapping("/product/category/list/tree")
    R listCategory();
}
