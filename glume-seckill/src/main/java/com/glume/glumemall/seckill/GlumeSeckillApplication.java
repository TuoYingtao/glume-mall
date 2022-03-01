package com.glume.glumemall.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GlumeSeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeSeckillApplication.class, args);
    }

}
