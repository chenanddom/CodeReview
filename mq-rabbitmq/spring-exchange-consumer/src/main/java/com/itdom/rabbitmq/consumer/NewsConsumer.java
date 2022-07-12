package com.itdom.rabbitmq.consumer;

import com.google.gson.Gson;
import com.itdom.rabbitmq.entity.News;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NewsConsumer {
    public void receiveNews(News news){
        System.out.println("接收到最新的新闻"+new Gson().toJson(news));
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }
}
