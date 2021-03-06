package com.glume.glumemall.order.service.impl;

import com.glume.glumemall.order.entity.OrderEntity;
import com.glume.glumemall.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author tuoyingtao
 * @create 2022-02-11 14:57
 */
@RabbitListener(queues = {"hello-java-Queue"})
@Service
public class RabbitServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    /**
     * @RabbitListener 注解: 声明需要监听的所有队列
     * @RabbitHandler 注解： 能够重载接受不同的消息类型的方法
     * @param message 原生消息详细信息  头+体
     * @param content T<发送消息的类型> 会话体内容
     * @param channel 当前传输数据的通道
     */
    @RabbitHandler
    public void recieveMessage(Message message, OrderReturnReasonEntity content, Channel channel) {
        LOGGER.info("接受到消息...==>内容：{}",content);
        /** 签收货物；手动确认ACK机制 */
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if (deliveryTag % 2 == 0) {
                /** 确认接受 basicAck( id，是否为批量确认); */
                channel.basicAck(deliveryTag,false);
            } else {
                /** 拒绝接受 basicNack( id，是否为批量拒绝，是否重写放回requeue中); */
                channel.basicNack(deliveryTag,false,false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void recieveMessage(Message message, OrderEntity content, Channel channel) {
        LOGGER.info("接受到消息...==>内容：{}",content);
    }
}
