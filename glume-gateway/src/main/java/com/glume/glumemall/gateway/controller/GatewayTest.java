package com.glume.glumemall.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tuoyingtao
 * @create 2021-10-14 16:16
 */
@RestController
public class GatewayTest {
    @GetMapping("/hello")
    public void hello() {
        System.out.println("hello");
    }
    @GetMapping("/qq")
    public void qq() {
        System.out.println("qq");
    }
}
