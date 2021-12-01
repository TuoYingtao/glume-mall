package com.glume.glumemall.product.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author TuoYingtao
 * @create 2021-12-01 21:31
 */
public class IndexController {

    @GetMapping({"","/","/index.html"})
    public String indexPage(Model model) {
        System.out.println("index");
        model.addAttribute("您好");
        return "index";
    }
}
