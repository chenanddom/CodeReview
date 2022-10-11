package com.itdom.consumer;

import com.itdom.config.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听order的消息
 *
 * @Date 11/8/2022 23:09:22
 */
@Component
@Slf4j
public class OnOrderListener {
    @RabbitListener(queues= MqConstant.DEAD_QUEUE)
    public void onOrderToResetStock(Message text, Channel channel) throws IOException {
        log.info("RabbitListener--message:{}",text.getBody());
        channel.basicAck(text.getMessageProperties().getDeliveryTag(),false);
    }

}
