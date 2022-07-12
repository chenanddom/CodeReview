package com.itdom.rabbitmq.topic;

import com.itdom.rabbitmq.constant.RabbitmqConstants;
import com.itdom.rabbitmq.utils.RabbitmqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class SinaConsumer {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.basicQos(1);
        channel.queueDeclare(RabbitmqConstants.WEATHER_SINA, false, false, false, null);
        channel.queueBind(RabbitmqConstants.WEATHER_SINA, RabbitmqConstants.WEATHER_EXCHANGE_TOPIC, "cn.guangxi.*.20220711");
        channel.basicConsume(RabbitmqConstants.WEATHER_SINA, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String s = new String(body);
                System.out.println(s);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
