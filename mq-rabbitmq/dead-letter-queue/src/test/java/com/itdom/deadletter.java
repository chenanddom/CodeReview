package com.itdom;

import com.itdom.config.MqConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeadLetterQueueApplication.class)
public class deadletter {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void sendMsg(){
        rabbitTemplate.convertAndSend(MqConstant.NORMAL_EXCHANGE, MqConstant.NORMAL_ROUTE, "ooooooooo");
    }


}
