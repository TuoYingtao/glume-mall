package com.glume.glumemall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableRabbit
@MapperScan("com.glume.glumemall.order.dao")
@SpringBootApplication
@EnableFeignClients(basePackages = "com.glume.glumemall.order.feign")
public class GlumeOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeOrderApplication.class, args);
    }

}
