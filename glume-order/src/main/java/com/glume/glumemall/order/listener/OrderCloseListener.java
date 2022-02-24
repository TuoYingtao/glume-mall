package com.glume.glumemall.order.listener;

import com.glume.glumemall.order.entity.OrderEntity;
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
 * 订单延迟关闭监听
 * @author tuoyingtao
 * @create 2022-02-24 17:16
 */
@Service
@RabbitListener(queues = {"order.release.order.queue"})
public class OrderCloseListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCloseListener.class);

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
        LOGGER.info("收到过期的订单信息：准备关闭订单" + orderEntity.getOrderSn());
        try {
            orderService.closeOrder(orderEntity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            // 拒绝接受消息，并重新加入到队列中
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
