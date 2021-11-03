package com.glume.glumemall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.glume.glumemall.admin", "com.glume.glumemall.common"})
@EnableDiscoveryClient
@SpringBootApplication
public class GlumeAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeAdminApplication.class, args);
    }

}
