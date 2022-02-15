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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String submitOrder(OrderSubmitVo orderSubmitVo, Model model, RedirectAttributes redirectAttributes) {
        SubmitOrderResponseVo submitOrderResponseVo = orderService.submitOrder(orderSubmitVo);
        if (submitOrderResponseVo.getCode() == 0) {
            // 下单成功
            model.addAttribute("submitOrderResponseVo",submitOrderResponseVo);
            return "pay";
        }
        String msg = "下单失败：";
        switch (submitOrderResponseVo.getCode()) {
            case 1:
                msg += "订单信息已过期，请重新提价！";
                break;
            case 2:
                msg += "订单商品价格发生变换，请确认后再次提交";
                break;
            case 3:
                msg += "库存锁定失败，商品库存不足";
                break;
        }
        redirectAttributes.addFlashAttribute("msg",msg);
        return "redirect:http://order.glumemall.com/toTrade";
    }
}
