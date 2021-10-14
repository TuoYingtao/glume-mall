package com.glume.glumemall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GlumeCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeCouponApplication.class, args);
    }

}
