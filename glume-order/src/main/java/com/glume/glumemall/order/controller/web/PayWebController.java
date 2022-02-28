package com.glume.glumemall.order.controller.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.glume.glumemall.order.config.AlipayTemplate;
import com.glume.glumemall.order.service.OrderService;
import com.glume.glumemall.order.vo.PayAsyncVo;
import com.glume.glumemall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    /**
     * 支付宝-支付同步通知
     */
    @ResponseBody
    @GetMapping(value = "/payOrder",produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVo payVo = orderService.getOrderPay(orderSn);
        // 返回的是一个页面
        String pay = alipayTemplate.pay(payVo);
        return pay;
    }

    /**
     * 支付宝-支付异步通知
     */
    @ResponseBody
    @PostMapping(value = "/payed/notify", produces = "text/html")
    public String handlerAliPayedListener(PayAsyncVo payAsyncVo, HttpServletRequest request) throws AlipayApiException {
        // 接受支付宝给我们的异步通知，告诉我们订单支付成功。当我们返回 success 后，支付宝就不再通知
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (Iterator<String> iter = parameterMap.keySet().iterator(); iter.hasNext();){
            String name = (String) iter.next();
            String[] values = (String[]) parameterMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(),
                alipayTemplate.getCharset(), alipayTemplate.getSign_type());
        // 支付宝签名验证
        if (signVerified) {
            String result = orderService.handlerPayResult(payAsyncVo);
            return result;
        } else {
            return "error";
        }
    }

}
