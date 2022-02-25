package com.glume.glumemall.order.controller.web;

import com.alipay.api.AlipayApiException;
import com.glume.glumemall.order.config.AlipayTemplate;
import com.glume.glumemall.order.service.OrderService;
import com.glume.glumemall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 订单支付
 * @author tuoyingtao
 * @create 2022-02-25 17:19
 */
@Controller
public class PayWebController {

    @Autowired
    AlipayTemplate alipayTemplate;

    @Autowired
    OrderService orderService;

    @ResponseBody
    @GetMapping(value = "/payOrder",produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVo payVo = orderService.getOrderPay(orderSn);
        // 返回的是一个页面
        String pay = alipayTemplate.pay(payVo);
        return pay;
    }
}
