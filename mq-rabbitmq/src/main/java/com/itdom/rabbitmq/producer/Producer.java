package com.itdom.rabbitmq.producer;

import com.itdom.rabbitmq.constant.RabbitmqConstants;
import com.itdom.rabbitmq.utils.RabbitmqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建ConnectionFactory用于创建物理链接
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("127.0.0.1");
//        factory.setPort(5672);
//        factory.setUsername("itdom");
//        factory.setPassword("123456");
//        factory.setVirtualHost("/itdom");
//        //TCP物理链接
//        Connection connection = factory.newConnection();
        Connection connection = RabbitmqUtil.getConnection();
        //创建通信“通道”，相当于TCP的虚拟链接
        Channel channel = connection.createChannel();
        //创建队列，声明并创建一个队列，如果该队列已经存在，则使用该队列
        /*
        参数1，队列的名称ID
        参数2，是否持久化数据，false表示不持久化数据，MQ服务器停掉之后数据就丢失
        参数3，队列是否私有化，false代表所有消费者可以使用，true表示第一个消费者才有资格访问
        参数4,链接停掉后是否自动删除队列
        参数5,其他参数
         */
//        AMQP.Queue.DeclareOk queue = channel.queueDeclare("hello_queue", false, false,false,null);
        AMQP.Queue.DeclareOk queue = channel.queueDeclare(RabbitmqConstants.QUEUE_HELLO, false, false,false,null);
        /**
         * 四个参数
         * 1.exchange,交换机
         * 2.队列名称
         * 3.额外的属性
         * 5.消息的具体内容
         */
        String message = "测试的消息";
//        channel.basicPublish("","hello_queue",null,message.getBytes());
        channel.basicPublish("",RabbitmqConstants.QUEUE_HELLO,null,message.getBytes());
        channel.close();
        connection.close();
        System.out.println("发送数据成功");
    }
}
