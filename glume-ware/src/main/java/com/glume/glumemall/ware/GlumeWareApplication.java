package com.glume.glumemall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@MapperScan("com.glume.glumemall.ware.dao")
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
@ComponentScans({
        @ComponentScan("com.glume.common.core"),
        @ComponentScan("com.glume.common.mybatis.config"),
        @ComponentScan("com.glume.common.swagger.config"),
        @ComponentScan("com.glume.common.validator.config")
})
public class GlumeWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeWareApplication.class, args);
    }

}
