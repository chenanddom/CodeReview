package com.itdom.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitmqUtil {
    private static ConnectionFactory factory = null;

    static {
        factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("itdom");
        factory.setPassword("123456");
        factory.setVirtualHost("/itdom");
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = factory.newConnection();
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
