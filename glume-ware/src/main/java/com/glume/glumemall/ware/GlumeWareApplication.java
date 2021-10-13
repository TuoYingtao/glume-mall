package com.glume.glumemall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.glume.glumemall.ware.dao")
@SpringBootApplication
public class GlumeWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeWareApplication.class, args);
    }

}
