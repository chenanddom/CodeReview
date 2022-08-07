package com.itdom.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class CustomProducerCallback {
    private static final Logger logger = LoggerFactory.getLogger(CustomProducerCallback.class);
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 0 配置
        Properties properties = new Properties();

        // 连接集群 bootstrap.servers
//        properties.put(/*ProducerConfig.BOOTSTRAP_SERVERS_CONFIG*/"bootstrap.servers","kafa-node1:9092,kafa-node2:9092,kafa-node3:9092");
        properties.put(/*ProducerConfig.BOOTSTRAP_SERVERS_CONFIG*/"bootstrap.servers","192.168.0.190:9092,192.168.0.191:9092,192.168.0.192ss:9092");

        // 指定对应的key和value的序列化类型 key.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        // 1 创建kafka生产者对象
        // "" hello
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        // 2 发送数据
        for (int i = 0; i < 10; i++) {
            //又发送
            kafkaProducer.send(new ProducerRecord<String, String>("first", "kafkamessage" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null){
                        System.out.println("主题： "+metadata.topic() + " 分区： "+ metadata.partition());
                    }                }
            });
            //同步发送
//            kafkaProducer.send(new ProducerRecord<String,String>("first","kafkamessage"+i)).get();
        }
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String next = scanner.next();
        }

        // 3 关闭资源
        kafkaProducer.close();
    }
}
