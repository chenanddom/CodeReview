package com.itdom.utils;

import com.itdom.configuration.MqttConfig;
import com.itdom.enums.QosEnum;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class EmqttClient {
    private static final Logger logger = LoggerFactory.getLogger(EmqttClient.class);

    private IMqttClient iMqttClient;
    @Autowired
    private MqttConfig mqttConfig;
    @Autowired
    private MqttCallback mqttCallback;

    @PostConstruct
    public void init() {
        MqttClientPersistence mempersitence = new MemoryPersistence();
        try {
            iMqttClient = new MqttClient(mqttConfig.getBrokerUrl(), mqttConfig.getClientId(), mempersitence);
        } catch (MqttException e) {
            logger.error("初始化客户端mqttClient对象失败,errormsg={},brokerUrl={},clientId={}", e.getMessage(), mqttConfig.getBrokerUrl(), mqttConfig.getClientId());
        }
    }

    public void connect(String username, String password) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        //设置临时会话
        options.setCleanSession(true);
        iMqttClient.setCallback(mqttCallback);
        try {
            iMqttClient.connect(options);
        } catch (MqttException e) {
            logger.error("mqtt客户端连接服务端失败,失败原因{}", e.getMessage());
        }
    }

    /**
     * 断开连接
     */
    @PreDestroy
    public void disConnect() {
        try {
            iMqttClient.disconnect();
        } catch (MqttException e) {
            logger.error("断开连接产生异常,异常信息{}", e.getMessage());
        }
    }

    /**
     * 重连
     */
    public void reConnect() {
        try {
            iMqttClient.reconnect();
        } catch (MqttException e) {
            logger.error("重连失败，原因:{}", e.getMessage());
        }
    }

    public void publish(String topic, String msg, QosEnum qosEnum, boolean retain) {

        MqttMessage message = new MqttMessage();
        message.setPayload(msg.getBytes());
        message.setQos(qosEnum.value());
        message.setRetained(retain);
        try {
            iMqttClient.publish(topic, message);
        } catch (MqttException e) {
            logger.error("mqtt客户端连接服务端失败,失败原因{}", e.getMessage());
        }
    }

    /**
     * 订阅主题
     *
     * @param topicFilter
     * @param qos
     */
    public void subscribe(String topicFilter, QosEnum qos) {

        try {
            iMqttClient.subscribe(topicFilter, qos.value());
        } catch (MqttException e) {
            logger.error("订阅主题失败,errormsg={},topicFilter={},qos={}", e.getMessage(), topicFilter, qos.value());
        }
    }


    /**
     * 取消订阅
     *
     * @param topicFilter
     */
    public void unSubscribe(String topicFilter) {
        try {
            iMqttClient.unsubscribe(topicFilter);
        } catch (MqttException e) {
            logger.error("取消订阅失败,errormsg={},topicfiler={}", e.getMessage(), topicFilter);
        }
    }
}
