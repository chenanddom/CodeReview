package com.itdom.consumer;

import com.itdom.rabbitmq.entity.Employee;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class MessageConsumer {
    /**
     * @RabbitListener 注解用于声明式定义消息接收队列与exchange绑定的信息
     * 消费者这端使用注解获取信息
     * @param employee
     */
    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "springboot-queue",durable = "true"),
            exchange = @Exchange(value = "springboot-exchange",durable = "true",type = "topic"),
            key = "#")
    )
    /**
     * 用于接收消息的方法
     * @RabbitHandler用于通知SpringBoot下面的方法用于接收消息，这个方法运行后将处于等待的状态，
     * 有新的消息进来会触发handleMessage方法的执行
     */
    @RabbitHandler
    public void handleMessage(@Payload Employee employee, Channel channel, @Headers Map<String, Object> headers){
        System.out.println("=====================================================");
        System.out.println("接收到:"+employee.getEmpno()+":"+employee.getName());
        long tagId = (long)(headers.get(AmqpHeaders.DELIVERY_TAG));
        try {
            channel.basicAck(tagId,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("=====================================================");
    }

}
