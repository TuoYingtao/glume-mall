package com.glume.glumemall.product.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author TuoYingtao
 * @create 2021-12-02 23:16
 */
@Controller
public class ItemController {

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId) {
        System.out.println("准备查询");
        return "item";
    }
}
