package com.glume.glumemall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScans({
        @ComponentScan("com.glume.common.core")
})
public class GlumemallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumemallThirdPartyApplication.class, args);
    }

}
