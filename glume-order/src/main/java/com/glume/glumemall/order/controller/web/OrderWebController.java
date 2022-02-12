package com.glume.glumemall.order.controller.web;

import com.glume.glumemall.order.service.OrderService;
import com.glume.glumemall.order.vo.OrderConfirmVo;
import com.glume.glumemall.order.vo.OrderSubmitVo;
import com.glume.glumemall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.ExecutionException;

/**
 * 购物车结算
 * @author tuoyingtao
 * @create 2021-12-16 9:49
 */
@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData",orderConfirmVo);
        return "confirm";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmitVo) {
        SubmitOrderResponseVo submitOrderResponseVo = orderService.submitOrder(orderSubmitVo);
        if (submitOrderResponseVo.getCode() == 0) {
            // 下单成功
            return "pay";
        }
        return "redirect:http://order.glumall.com/toTrade";
    }
}
