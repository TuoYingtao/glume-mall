package com.glume.glumemall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.glume.glumemall.member.dao")
@SpringBootApplication
public class GlumeMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeMemberApplication.class, args);
    }

}
