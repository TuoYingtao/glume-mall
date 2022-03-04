package com.glume.glumemall.product.feign.failBack;

import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.glumemall.product.feign.SeckillFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author tuoyingtao
 * @create 2022-03-04 11:49
 */
@Component
public class SeckillFeignServiceFailBack implements SeckillFeignService {
    private final Logger LOGGER = LoggerFactory.getLogger(SeckillFeignServiceFailBack.class);

    @Override
    public R getSkuSeckillInfo(Long skuId) {
        LOGGER.info("熔断方法调用==>getSkuSeckillInfo");
        return R.error(HttpStatus.BizCodeEnum.TO_MANY_REQUEST.getCode(), HttpStatus.BizCodeEnum.TO_MANY_REQUEST.getMsg());
    }
}
