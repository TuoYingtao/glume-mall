package com.glume.glumemall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.glume.glumemall.product","com.glume.glumemall.common"})
public class GlumeProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeProductApplication.class, args);
    }

}
