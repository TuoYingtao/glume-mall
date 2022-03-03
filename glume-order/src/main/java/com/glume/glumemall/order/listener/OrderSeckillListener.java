package com.glume.glumemall.order.listener;

import com.glume.common.core.to.mq.SeckillOrderTo;
import com.glume.glumemall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 秒杀订单监听
 * @author tuoyingtao
 * @create 2022-03-03 17:46
 */
@Service
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSeckillListener {
    private final Logger LOGGER = LoggerFactory.getLogger(OrderSeckillListener.class);

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void createSeckillOrder(SeckillOrderTo seckillOrderTo, Channel channel, Message message) throws IOException {
        LOGGER.info("已监听秒杀商品订单，开始生成详细信息");
        try {
            orderService.createSeckillOrder(seckillOrderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
