package com.itdom.consumer;

import com.itdom.config.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听order的消息
 *
 * @Date 11/8/2022 23:09:22
 */
@Component
@Slf4j
public class OnOrderListener {
    @RabbitListener(queues= MqConstant.DEAD_QUEUE)
    public void onOrderToResetStock(String text){
        log.info("RabbitListener--message:{}",text);
    }

}
