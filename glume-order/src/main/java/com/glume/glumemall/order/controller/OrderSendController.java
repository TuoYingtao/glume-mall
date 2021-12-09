package com.glume.glumemall.order.controller;

import com.glume.glumemall.order.entity.OrderEntity;
import com.glume.glumemall.order.entity.OrderReturnReasonEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author tuoyingtao
 * @create 2021-12-09 11:57
 */
@Controller
public class OrderSendController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSendController.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{num}")
    @ResponseBody
    public String send(@PathVariable("num") String num) {
        for (int i = 0; i < Integer.parseInt(num); i++) {
            if (i%2 == 0) {
                OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
                reasonEntity.setId(1L);
                reasonEntity.setCreateTime(new Date());
                reasonEntity.setName("哈哈");
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",reasonEntity);
                LOGGER.info("消息发送完成",reasonEntity);
            } else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderSn("aaa");
                orderEntity.setBillContent("aaaa");
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",orderEntity);
                LOGGER.info("消息发送完成",orderEntity);
            }
        }
        return "ok";
    }
}
