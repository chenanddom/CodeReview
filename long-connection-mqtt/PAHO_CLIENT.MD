# Paho

[项目代码](./mqtt-paho-client)

引入依赖
pom.xml
````xml
        <dependency>
            <groupId>org.eclipse.paho</groupId>
            <artifactId>org.eclipse.paho.client.mqttv3</artifactId>
            <version>1.2.2</version>
        </dependency>
````

* 基于paho的客户端封装

[基于paho的客户端封装](./mqtt-paho-client/src/main/java/com/itdom/utils/EmqttClient.java)
```java
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
    /**
     * mqtt发生数据成功、接收数据或者丢失和broker的连接都会进行响应的方法的回调，在回调的方法内可以实现相关的业务操作
     */
    @Autowired
    private MqttCallback mqttCallback;

    /**
     * 初始化paho客户端
     */
    @PostConstruct
    public void init() {
        //推送的和接收到的消息的一个持久化类，确保消息的质量
        MqttClientPersistence mempersitence = new MemoryPersistence();
        try {
            iMqttClient = new MqttClient(mqttConfig.getBrokerUrl(), mqttConfig.getClientId(), mempersitence);
        } catch (MqttException e) {
            logger.error("初始化客户端mqttClient对象失败,errormsg={},brokerUrl={},clientId={}", e.getMessage(), mqttConfig.getBrokerUrl(), mqttConfig.getClientId());
        }
    }

    /**
     * 建立连接
     * @param username
     * @param password
     */
    public void connect(String username, String password) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        //自动重连
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

```

* MqttCallback接口的实现

[MqttCallback接口的实现](./mqtt-paho-client/src/main/java/com/itdom/utils/MessageCallback.java)

```java
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
```
