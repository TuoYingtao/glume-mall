package com.glume.glumemall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScans({
        @ComponentScan("com.glume.common.core"),
        @ComponentScan({"com.glume.common.seata"}),
        @ComponentScan("com.glume.common.validator.config"),
})
public class GlumeCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeCouponApplication.class, args);
    }

}
