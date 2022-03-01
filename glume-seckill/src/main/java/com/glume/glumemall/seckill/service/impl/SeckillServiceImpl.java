package com.glume.glumemall.seckill.service.impl;

import com.glume.glumemall.seckill.feign.CouponFeignService;
import com.glume.glumemall.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 上传秒杀活动
 * @author tuoyingtao
 * @create 2022-03-01 17:20
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Override
    public void uploadSeckillSkuLatest3Day() {
        // 扫描最近三天需要参与秒杀的活动
    }
}
