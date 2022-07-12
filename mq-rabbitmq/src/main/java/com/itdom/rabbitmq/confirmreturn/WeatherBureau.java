package com.itdom.rabbitmq.confirmreturn;

import com.itdom.rabbitmq.constant.RabbitmqConstants;
import com.itdom.rabbitmq.utils.RabbitmqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
        map.put("cn.guangxi.nanning.20220711","南宁2022.07.11的天气数据");
        map.put("cn.guangxi.beihai.20220711","北海2022.07.11的天气数据");
        //开启Confirm监听模式
        channel.confirmSelect();
        //开启监听，此处需要注意，如果关闭了通信的通道和链接，就无法接收到confirm消息
        channel.addConfirmListener(new ConfirmListener() {
            /**
             *
             * @param deliveryTag 消息的tag
             * @param multiple 是否批量接收，一般情况不用
             * @throws IOException
             */
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息已经被Broker接收,Tag:"+deliveryTag);
            }

            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息被Broker拒收，Tag:"+deliveryTag);
            }
        });
        channel.addReturnListener(new ReturnCallback() {
            public void handle(Return returnMessage) {
                System.out.println("==============================================");
                System.out.println("错误编码:"+returnMessage.getReplyCode()+"-"+"错误描述:"+returnMessage.getReplyText());
                System.out.println("==============================================");

            }
        });
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            /**
             * mandatory，如果该参数设置为true时，如果消息无法正常处理就会原路返回，如果false，如果消息无法正常处理就不错处理
             */
            channel.basicPublish(RabbitmqConstants.WEATHER_EXCHANGE_TOPIC, key, true,null, map.get(key).getBytes());
        }
//        if (channel != null) {
//            channel.close();
//        }
//        if (connection != null) {
//            connection.close();
//        }
    }
}
