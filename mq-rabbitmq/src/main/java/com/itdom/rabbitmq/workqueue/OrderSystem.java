package com.itdom.rabbitmq.workqueue;

import com.google.gson.Gson;
import com.itdom.rabbitmq.constant.RabbitmqConstants;
import com.itdom.rabbitmq.utils.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OrderSystem {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitmqUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitmqConstants.QUEUE_SMS, false, false,false,null);
        for (int i = 0; i < 1000; i++) {
            String body = new Gson().toJson(new SMS("user:" + i, "180393000" + i, "你有新的短信:" + i));
            channel.basicPublish("", RabbitmqConstants.QUEUE_SMS,null,body.getBytes());
        }
        if (channel!=null){
            channel.close();
        }
        if (connection!=null){
            connection.close();
        }
        System.out.println("send message success!!");

    }
}
