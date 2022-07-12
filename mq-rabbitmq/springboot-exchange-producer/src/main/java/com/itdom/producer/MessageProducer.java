package com.itdom.producer;

import com.itdom.rabbitmq.entity.Employee;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class MessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;
    private RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * Confirm消息
         * @param correlationData 消息的附加消息，即自定义的ID
         * @param b 代表消息是否被broker（MQ）接收true代表接收 false代表拒收
         * @param s 如果时拒收，说明拒收的原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.out.println(correlationData);
            System.out.println(b);
            if (!b) {
                System.out.println(s);
            }

        }
    };
    private RabbitTemplate.ReturnsCallback returnsCallback = new RabbitTemplate.ReturnsCallback() {
        @Override
        public void returnedMessage(ReturnedMessage returnedMessage) {
            System.err.println("Code:" + returnedMessage.getReplyCode() + "-text:" + returnedMessage.getReplyText());
            System.err.println("Exchange:" + returnedMessage.getExchange() + "-RoutingKey:" + returnedMessage.getRoutingKey());
        }
    };

    public void sendMsg(Employee employee) {
        //CorrelationData对象的作用时作为消息的附加信息传递，通常我们用它来保存消息的自定义ID
        CorrelationData cd = new CorrelationData(employee.getEmpno() + "-" + new Date().getTime());
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnsCallback(returnsCallback);
        rabbitTemplate.convertAndSend("springboot-exchange", "hr.employee", employee, cd);
    }

}

