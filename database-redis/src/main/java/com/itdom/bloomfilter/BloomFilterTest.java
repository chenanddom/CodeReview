package com.itdom.bloomfilter;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 布隆过滤器
  */
public class BloomFilterTest {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redissonClient = Redisson.create(config);
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("skuIds");
        //初始化布隆过滤器。
        bloomFilter.tryInit(1000,0.01);
        List<String> list = new ArrayList<String>();
        int counter=0;
        for (int i = 0; i <1000 ; i++) {
            String content = UUID.randomUUID().toString();
            list.add(content);
            bloomFilter.add(content);
        }

        for (String s : list) {
            if (bloomFilter.contains(s)) {
                counter++;
            }
        }
        System.out.println("counter:"+counter);
        redissonClient.shutdown();
    }
}
