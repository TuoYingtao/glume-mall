package com.glume.glumemall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScans({
        @ComponentScan("com.glume.common.core"),
        @ComponentScan("com.glume.common.mybatis.config"),
        @ComponentScan("com.glume.common.swagger.config"),
        @ComponentScan("com.glume.common.validator.config")
})
public class GlumeMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeMemberApplication.class, args);
    }

}
