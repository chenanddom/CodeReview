package com.itdom.rabbitmq.workqueue;

import com.itdom.rabbitmq.constant.RabbitmqConstants;
import com.itdom.rabbitmq.utils.RabbitmqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class OrderReceiver3 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitmqConstants.QUEUE_SMS, false, false, false, null);
        //如果此处不设置basicQos(1),那么Rabbitmq服务器会平均的将消息发送给客户端消费，如果某些客户端的处理速度很慢，那么消息就会堆积，
        //如果设置了这个baiscQos(1)，那么就可以实现消费完一个在分配一个消息。
        channel.basicQos(1);
        channel.basicConsume(RabbitmqConstants.QUEUE_SMS, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String bodyContent = new String(body);
                System.out.println(bodyContent);
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }

}
