package com.itdom.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CustomerConsumerForSeek {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.190:9092,192.168.0.191:9092,192.168.0.192:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //配置消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "Kafka-group-1");
        //配置分区策略
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RangeAssignor");

        //默认最大的一批数据时50M
//        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,50*1024*1024);
        //一次拉取的最大记录数,默认是500
//        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,500);



        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        //订阅kafka消息队列的主题
        kafkaConsumer.subscribe(new ArrayList<String>() {{
            add("first");
        }});

        Set<TopicPartition> topicPartitionSet = new HashSet<>();
        while (topicPartitionSet.size() == 0) {
            kafkaConsumer.poll(Duration.ofSeconds(1));
            //获取所有分区信息，有了分区信息才可以进行消费
            topicPartitionSet = kafkaConsumer.assignment();
        }
        //遍历所有主题的分区，并将分区设置再200的位置
        for (TopicPartition topicPartition : topicPartitionSet) {
            kafkaConsumer.seek(topicPartition, 200);
        }
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
