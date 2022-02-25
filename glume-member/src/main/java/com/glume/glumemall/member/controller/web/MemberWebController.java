package com.glume.glumemall.member.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 个人订单列表
 * @author tuoyingtao
 * @create 2022-02-25 18:17
 */
@Controller
public class MemberWebController {

    @GetMapping("/memberOrder.html")
    public String memberOrderPage() {
        return "orderList";
    }
}
