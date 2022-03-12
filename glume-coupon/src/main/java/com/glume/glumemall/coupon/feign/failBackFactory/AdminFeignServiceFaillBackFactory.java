package com.glume.glumemall.coupon.feign.failBackFactory;

import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.glumemall.coupon.feign.AdminFeignService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author tuoyingtao
 * @create 2022-03-12 17:03
 */
@Component
public class AdminFeignServiceFaillBackFactory implements FallbackFactory<AdminFeignService> {
    private final Logger LOGGER = LoggerFactory.getLogger(AdminFeignServiceFaillBackFactory.class);

    private String server = "glume-admin";

    @Override
    public AdminFeignService create(Throwable throwable) {
        return new AdminFeignService() {
            @Override
            public R getByUserDetail(String username) {
                LOGGER.error("admin远程调用异常：使用用户名获取用户信息失败！=>{}",throwable.getMessage());
                return R.error(HttpStatus.BizCodeEnum.REMOTE_ERROR.getCode(), throwable.getMessage());
            }
        };
    }
}
