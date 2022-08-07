package com.itdom.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Scanner;

public class CustomerProducerTransactions {
    private static final Logger logger = LoggerFactory.getLogger(CustomProducerCallbackPartitions.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.190:9092,192.168.0.191:9092,192.168.0.192:9092");
        //键值对的序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 修改RecordAccumulator的双对队列的大小，默认是32M
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        //修改linger.ms 5~100ms是最好的范围
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        //修改批次的大小，默认是16k
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        //默认的有gzip,snappy,lz4,zstd,snappy用的最多
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        // 指定事务id(一定要指定)
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "kafka_transaction_001");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        //初始化事务
        kafkaProducer.initTransactions();
        //开启事务
        kafkaProducer.beginTransaction();
        try {
            for (int i = 0; i < 10; i++) {

                kafkaProducer.send(new ProducerRecord<String, String>("first", "transaction-00"+i), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (metadata != null) {
                            logger.info("topic:{},partition:{}", metadata.topic(), metadata.partition());
                        }
                    }
                });
            }
            //提交事务
            kafkaProducer.commitTransaction();
        } catch (Exception e) {
            //丢弃事务
            kafkaProducer.abortTransaction();
        } finally {
            kafkaProducer.close();
        }
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            scanner.nextInt();
        }
    }
}
