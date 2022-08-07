package com.itdom.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MyCustomerPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String v = value.toString();

        if(v.contains("kafka")){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void onNewBatch(String topic, Cluster cluster, int prevPartition) {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
