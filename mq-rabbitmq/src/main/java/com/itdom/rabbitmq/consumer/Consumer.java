package com.itdom.rabbitmq.consumer;

import com.itdom.rabbitmq.constant.RabbitmqConstants;
import com.itdom.rabbitmq.utils.RabbitmqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建ConnectionFactory用于创建物理链接
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("127.0.0.1");
//        factory.setPort(5672);
//        factory.setUsername("itdom");
//        factory.setPassword("123456");
//        factory.setVirtualHost("/itdom");
        //TCP物理链接
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
        AMQP.Queue.DeclareOk queue = channel.queueDeclare(RabbitmqConstants.QUEUE_HELLO, false, false,false,null);
        /**
         * 参数1:队列的名称
         * 参数2:是否自动确收接收消息,false代表手动进行确认(推荐)
         *
         */
        channel.basicConsume(RabbitmqConstants.QUEUE_HELLO,false,new MyReceiver(channel));
    }

}
class MyReceiver extends DefaultConsumer{
    private Channel channel;
    //重写构造函数,channel通道对象需要从外层获取，
    public MyReceiver(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//        super.handleDelivery(consumerTag, envelope, properties, body);
        String message = new String(body);
        System.out.println("message:"+message);
        //签收消息
        /**
         * 参数1:获取消息的tagID
         * 参数2:false代表只签收当前的消息，设置true代表没有签收的消息一并的签收
         */
        channel.basicAck(envelope.getDeliveryTag(),false);


    }
}
