package com.glume.glumemall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.glume.glumemall.order.dao")
@SpringBootApplication
public class GlumeOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeOrderApplication.class, args);
    }

}
