package com.glume.glumemall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GlumeGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeGatewayApplication.class, args);
    }

}
