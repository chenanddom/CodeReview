package com.itdom.rabbitmq.publishsubscribe;

import com.itdom.rabbitmq.constant.RabbitmqConstants;
import com.itdom.rabbitmq.utils.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅，气象局发布消息到交换机，交换机需要和队列绑定
 */
public class WeatherBureau {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitmqUtil.getConnection();
        Channel channel = connection.createChannel();
        Scanner scanner = new Scanner(System.in);
        String weatherContent = scanner.next();
        channel.basicPublish(RabbitmqConstants.WEATHER_EXCHANGE, "", null, weatherContent.getBytes());
        if (channel != null) {
            channel.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
