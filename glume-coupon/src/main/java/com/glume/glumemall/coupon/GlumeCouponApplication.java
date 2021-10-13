package com.glume.glumemall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDiscoveryClient
@MapperScan("com.glume.glumemall.coupon.dao")
@SpringBootApplication
public class GlumeCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeCouponApplication.class, args);
    }

}
