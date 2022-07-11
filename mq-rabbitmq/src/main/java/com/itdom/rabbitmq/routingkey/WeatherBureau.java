package com.itdom.rabbitmq.routingkey;

import com.itdom.rabbitmq.constant.RabbitmqConstants;
import com.itdom.rabbitmq.utils.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅，气象局发布消息到交换机，交换机需要和队列绑定
 */
public class WeatherBureau {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitmqUtil.getConnection();
        Channel channel = connection.createChannel();
//        Scanner scanner = new Scanner(System.in);
//        String weatherContent = scanner.next();
        Map<String,String> map = new HashMap<String, String>();
        map.put("cn.guangdong.shenzhen.20220711","深圳2022.07.11的天气数据");
        map.put("cn.guangdong.guangzhou.20220711","广州2022.07.11的天气数据");
        map.put("cn.guangdong.dongguan.20220711","东莞2022.07.11的天气数据");
        map.put("cn.guangdong.foshan.20220711","佛山2022.07.11的天气数据");
        map.put("cn.guangdong.zhuhai.20220711","珠海2022.07.11的天气数据");

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            channel.basicPublish(RabbitmqConstants.WEATHER_EXCHANGE_ROUTING, key, null, map.get(key).getBytes());
        }
        if (channel != null) {
            channel.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
