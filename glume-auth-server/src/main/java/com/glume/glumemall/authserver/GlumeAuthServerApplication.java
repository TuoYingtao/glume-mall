package com.glume.glumemall.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScans({
		@ComponentScan("com.glume.common.core"),
})
public class GlumeAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlumeAuthServerApplication.class, args);
	}

}
