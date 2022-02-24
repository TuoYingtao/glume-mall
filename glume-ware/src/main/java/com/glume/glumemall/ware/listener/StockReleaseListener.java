package com.glume.glumemall.ware.listener;

import com.glume.common.core.to.mq.StockLockedTo;
import com.glume.glumemall.ware.service.WareSkuService;
import com.glume.common.core.to.mq.OrderTo;
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
 * 库存解锁监听器
 * @author tuoyingtao
 * @create 2022-02-24 14:06
 */
@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockReleaseListener.class);

    @Autowired
    WareSkuService wareSkuService;

    /**
     * 库存自动解锁
     */
    @RabbitHandler
    public void handlerStockLockRelease(Channel channel, Message message, StockLockedTo stockLockedTo) throws IOException {
        LOGGER.debug("收到解锁库存的消息...");
        try {
            wareSkuService.handlerStockLockRelease(stockLockedTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 消息拒绝以后重新放入队列。
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    /**
     * 订单关闭主动解锁库存
     */
    @RabbitHandler
    public void handlerOrderLockRelease(Channel channel, Message message, OrderTo order) throws IOException {
        LOGGER.debug("订单关闭准备解锁库存...");
        try {
            wareSkuService.unlockStockOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 消息拒绝以后重新放入队列。
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
