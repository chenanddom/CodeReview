package com.itdom.utils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageCallback implements MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(MessageCallback.class);

    /**
     * 丢失了对服务端的连接后触发的回调
     *
     * @param cause
     */
    public void connectionLost(Throwable cause) {
        logger.info("丢失对服务端的连接");
    }

    /**
     * 应用收到消息后触发的回调
     *
     * @param topic
     * @param message
     * @throws Exception
     */
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        logger.info("订阅者订阅到了消息,topic={},messageid={},qos={},payload={}",
                topic,
                message.getId(),
                message.getQos(),
                new String(message.getPayload()));
    }

    /**
     * 消息发布者消息发布完成产生的回调
     *
     * @param token
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
        int messageId = token.getMessageId();
        String[] topics = token.getTopics();
        logger.info("消息发布完成,messageid={},topics={}", messageId, topics);
    }
}
