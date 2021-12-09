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
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.order.dao.OrderItemDao;
import com.glume.glumemall.order.entity.OrderItemEntity;
import com.glume.glumemall.order.service.OrderItemService;

@RabbitListener(queues = {"hello-java-Queue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @RabbitListener 注解: 声明需要监听的所有队列
     * @RabbitHandler 注解： 能够重载接受不同的消息类型的方法
     * @param message 原生消息详细信息  头+体
     * @param content T<发送消息的类型> 会话体内容
     * @param channel 当前传输数据的通道
     */
    @RabbitHandler
    public void recieveMessage(Message message, OrderReturnReasonEntity content, Channel channel) {
//        LOGGER.info("接受到消息...{}",message);
        LOGGER.info("接受到消息...==>内容：{}",content);
    }

    @RabbitHandler
    public void recieveMessage(Message message, OrderEntity content, Channel channel) {
//        LOGGER.info("接受到消息...{}",message);
        LOGGER.info("接受到消息...==>内容：{}",content);
    }

}