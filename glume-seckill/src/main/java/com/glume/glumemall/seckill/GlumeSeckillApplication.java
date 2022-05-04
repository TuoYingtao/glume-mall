package com.glume.glumemall.seckill;

import com.glume.glumemall.seckill.to.MailTo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScans({
        @ComponentScan("com.glume.common.core")
})
public class GlumeSeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeSeckillApplication.class, args);
    }

}
