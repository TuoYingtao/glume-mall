package com.glume.glumemall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.glume.glumemall.product.dao")
@SpringBootApplication
public class GlumeProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeProductApplication.class, args);
    }

}
