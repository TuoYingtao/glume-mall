package com.glume.glumemall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@ComponentScans({
        @ComponentScan("com.glume.common.core"),
        @ComponentScan("com.glume.common.mybatis.config"),
        @ComponentScan("com.glume.common.swagger.config"),
        @ComponentScan("com.glume.common.validator.config")
})
@EnableFeignClients(basePackages = "com.glume.glumemall.product.feign")
public class GlumeProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeProductApplication.class, args);
    }

}
