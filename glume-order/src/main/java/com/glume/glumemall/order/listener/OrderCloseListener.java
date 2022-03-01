package com.glume.glumemall.order.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.glume.glumemall.order.config.AlipayTemplate;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    @Autowired
    AlipayTemplate alipayTemplate;

    @RabbitHandler
    public void listener(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
        LOGGER.info("收到过期的订单信息：准备关闭订单" + orderEntity.getOrderSn());
        try {
            orderService.closeOrder(orderEntity);
            /** 手动调用支付宝收单 */
//            alipayTradeClose(request);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            // 拒绝接受消息，并重新加入到队列中
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    private void alipayTradeClose(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayTemplate.getGatewayUrl(),
                alipayTemplate.getApp_id(),
                alipayTemplate.getMerchant_private_key(),
                "json",
                alipayTemplate.getCharset(),
                alipayTemplate.getAlipay_public_key(),
                alipayTemplate.getSign_type());
        //设置请求参数
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTCout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDTCtrade_no").getBytes("ISO-8859-1"),"UTF-8");
        //请二选一设置
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," +"\"trade_no\":\""+ trade_no +"\"}");
        //请求
        String result = alipayClient.execute(alipayRequest).getBody();
        //输出
        LOGGER.info("手动调用支付宝收单：{}",result);
    }
}
