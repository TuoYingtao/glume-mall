package com.glume.glumemall.member.controller.web;

import com.alibaba.fastjson.JSON;
import com.glume.common.core.utils.R;
import com.glume.glumemall.member.feign.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 个人订单列表
 * @author tuoyingtao
 * @create 2022-02-25 18:17
 */
@Controller
public class MemberWebController {

    @Autowired
    OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam Map<String,Object> params, Model model) {
        R result = orderFeignService.listWithItem(params);
        model.addAttribute("order",result);
        System.out.println(JSON.toJSONString(result));
        return "orderList";
    }
}

