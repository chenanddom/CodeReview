package com.itdom.rabbitmq.producer;

import com.itdom.rabbitmq.entity.News;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class NewsProducer {
    private RabbitTemplate rabbitTemplate = null;

    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishNews(String routingKey, News news) {
        rabbitTemplate.convertAndSend(routingKey, news);
        System.out.println("推送新闻成功！！！");
    }


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        NewsProducer newsProducer = (NewsProducer) context.getBean("newsProducer");
        newsProducer.publishNews("cn.20220712",new News("新华社","深圳天气高温预警",new Date(System.currentTimeMillis()),"深圳天气高温预警!!!!"));
        newsProducer.publishNews("us.20220712",new News("凤凰卫视","安倍被刺杀",new Date(System.currentTimeMillis()),"安倍奈良市演讲被刺杀身亡!!!!"));
    }

}
