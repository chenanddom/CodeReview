package com.itdom.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

/**
 * 指定消费的主题的某个分区进行消费
 */
public class CustomerConsumerForSpecialPartition {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.190:9092,192.168.0.191:9092,192.168.0.192:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //配置消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "Kafka-group-1");
        //配置分区策略
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RangeAssignor");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        //订阅kafka消息队列的主题
//        kafkaConsumer.subscribe(new ArrayList<String>() {{
//            add("first");
//        }});
        kafkaConsumer.assign(new ArrayList<TopicPartition>(){{add(new TopicPartition("first",1));}});

        // 3 消费数据
        while (true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println(consumerRecord);
            }
            kafkaConsumer.commitAsync();
        }
    }
}
