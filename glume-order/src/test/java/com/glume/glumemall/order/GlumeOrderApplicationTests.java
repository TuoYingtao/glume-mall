package com.glume.glumemall.order;

import com.glume.glumemall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@Slf4j
@SpringBootTest
class GlumeOrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void sendMessageTest() {
        OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
        reasonEntity.setId(1L);
        reasonEntity.setCreateTime(new Date());
        reasonEntity.setName("哈哈");
        rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",reasonEntity);
        log.info("消息发送完成",reasonEntity);
    }

    @Test
    void createExchange() {
        DirectExchange directExchange = new DirectExchange("hello-java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange【{}】创建成功","hello-java-exchange");
    }

    @Test
    void createQueue() {
        Queue queue = new Queue("hello-java-Queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue【{}】创建成功","hello-java-Queue");
    }

    @Test
    void createBinding() {
        Binding binding = new Binding("hello-java-Queue",
                Binding.DestinationType.QUEUE,"hello-java-exchange","hello.java",null);
        amqpAdmin.declareBinding(binding);
        log.info("binding【{}】创建成功","hello-java-binding");
    }

    @Test
    void delBinding() {
        Binding binding = new Binding("hello-java-Queue",Binding.DestinationType.QUEUE,
                "hello-java-exchange","hello.java",null);
        amqpAdmin.removeBinding(binding);
        log.info("binding【{}】删除成功","hello-java-binding");
    }

    @Test
    void delQueue() {
        amqpAdmin.deleteQueue("hello-java-Queue");
        log.info("Queue【{}】删除成功","hello-java-binding");
    }

    @Test
    void delExchange() {
        amqpAdmin.deleteExchange("hello-java-exchange");
        log.info("Exchange【{}】删除成功","hello-java-exchange");
    }
}
