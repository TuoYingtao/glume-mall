package com.glume.glumemall.admin.feign.failBackFactroy;

import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.glumemall.admin.feign.CouponFeignPromotionService;
import com.glume.glumemall.admin.to.SeckillPromotionTo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2022-03-15 17:15
 */
@Component
public class CouponFeignPromotionFailBackFactory implements FallbackFactory<CouponFeignPromotionService> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponFeignPromotionFailBackFactory.class);


    @Override
    public CouponFeignPromotionService create(Throwable cause) {
        LOGGER.error(cause.getMessage());
        return new CouponFeignPromotionService() {
            @Override
            public R list(Map<String, Object> params) {
                return defaultError();
            }

            @Override
            public R save(SeckillPromotionTo seckillPromotion) {
                return defaultError();
            }

            @Override
            public R info(Long id) {
                return defaultError();
            }

            @Override
            public R update(SeckillPromotionTo seckillPromotion) {
                return defaultError();
            }

            @Override
            public R delete(Long[] ids) {
                return defaultError();
            }
        };
    }

    public R defaultError() {
        return R.error(HttpStatus.BizCodeEnum.REMOTE_ERROR.getCode(), "网络波动，秒杀活动主题信息保存失败！");
    }
}

