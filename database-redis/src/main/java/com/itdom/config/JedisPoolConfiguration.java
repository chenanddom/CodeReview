package com.itdom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisPoolConfiguration {
    @Bean("jedisPool")
    public JedisPool jedisPool() {
        //创建连接池配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);//设置最大连接数
        config.setMaxIdle(10);
        config.setMinIdle(3);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        JedisPool pool = new JedisPool(config, "192.168.0.199", 6379, 1000, "root@123");
        return pool;
    }
}
