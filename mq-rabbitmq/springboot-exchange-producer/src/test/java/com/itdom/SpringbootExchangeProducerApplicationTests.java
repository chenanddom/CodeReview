package com.itdom;

import com.itdom.producer.MessageProducer;
import com.itdom.rabbitmq.entity.Employee;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.annotation.security.RunAs;

@SpringBootTest
@RunWith(SpringRunner.class)
class SpringbootExchangeProducerApplicationTests {
    @Resource
    private MessageProducer messageProducer;
    @Test
    void contextLoads() {
    }
    @Test
    public void sendMsg(){
        messageProducer.sendMsg(new Employee("15672","chendom",20));
    }

}
