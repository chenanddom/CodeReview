package com.itdom.config;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mq配置类 声明队列交换机
 * 令外，方法名就是 bean的id
 *
 * @Date 11/8/2022 23:14:42
 */
@Configuration
public class MqConfig {
    //声明正常队列
    @Bean
    public Queue normalQueue(){
        return QueueBuilder.durable(MqConstant.NORMAL_QUEUE)
                .withArgument("x-dead-letter-exchange", MqConstant.DEAD_EXCHANGE)//设置死信交换机
                .withArgument("x-message-ttl", MqConstant.TTL_TIME)
                .withArgument("x-dead-letter-routing-key", MqConstant.DEAD_ROUTE)//设置死信routingKey
                .build();
    }
    //声明死信队列
    @Bean
    public Queue deadQueue(){
//        return QueueBuilder.durable(MqConstant.DEAD_QUEUE).build();
        return new Queue(MqConstant.DEAD_QUEUE);
    }

    //声明正常交换机
    @Bean
    public DirectExchange normalExchange(){
        return ExchangeBuilder.directExchange(MqConstant.NORMAL_EXCHANGE)
                .durable(true).build();
    }

    //声明死信交换机
    @Bean
    public DirectExchange deadExchange(){
        return ExchangeBuilder.directExchange(MqConstant.DEAD_EXCHANGE)
                .durable(true).build();
    }

    //正常交换机-->正常队列
    @Bean
    public Binding normalBinding(){
        return BindingBuilder.bind(normalQueue())
                .to(normalExchange())
                .with(MqConstant.NORMAL_ROUTE);
    }

    //死信交换机-->死信队列
    // (正常队列 超时后 ——> 死信交换机->死信队列 )-->
    // 因此，向正常交换机发消息，监听死信队列，就可以实现消息延时消费
    @Bean
    public Binding deadBinding(){
        return BindingBuilder.bind(deadQueue())
                .to(deadExchange())
                .with(MqConstant.DEAD_ROUTE);
    }
}