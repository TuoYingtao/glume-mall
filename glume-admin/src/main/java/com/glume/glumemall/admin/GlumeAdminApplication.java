package com.glume.glumemall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GlumeAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeAdminApplication.class, args);
    }

}
