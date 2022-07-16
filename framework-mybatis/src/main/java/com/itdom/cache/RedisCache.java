package com.itdom.cache;

import org.apache.ibatis.cache.Cache;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * String:key-->value
 * hash:key-->feild-->value
 */
public class RedisCache implements Cache {
    //mybatis自动生成的唯一标识
    private final String id;


    private String mybatisKey = "mybatisKey";

    private String host;
    private int port;
    private String password;

    public RedisCache(String id) {
        this.id = id;
    }

    private Jedis jedis() {
        Jedis jedis = new Jedis(host, port);
        if (password != null && password != "") {
            jedis.auth(password);
        }
        return jedis;
    }


    @Override
    public String getId() {
        return id;
    }

    private byte[] object2Bytes(Object obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oop = new ObjectOutputStream(baos);
            oop.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    private Object bytes2Object(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            return objectInputStream.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void putObject(Object key, Object value) {
        Jedis jedis = jedis();
        try {
            jedis.hsetnx(mybatisKey.getBytes(), object2Bytes(key), object2Bytes(value));
        } finally {
            //关闭链接
            jedis.close();
        }
    }

    @Override
    public Object getObject(Object key) {
        Jedis jedis = jedis();
        try {
            byte[] ret = jedis.hget(mybatisKey.getBytes(), object2Bytes(key));
            return bytes2Object(ret);
        } finally {
            //关闭链接
            jedis.close();
        }
    }

    @Override
    public Object removeObject(Object key) {
        Jedis jedis = jedis();
        try {
            byte[] ret = jedis.hget(mybatisKey.getBytes(), object2Bytes(key));
            jedis.hdel(mybatisKey.getBytes(), object2Bytes(key));
            return bytes2Object(ret);
        } finally {
            //关闭链接
            jedis.close();
        }
    }

    @Override
    public void clear() {
        Jedis jedis = jedis();
        try {
            jedis.del(mybatisKey);
        } finally {
            //关闭链接
            jedis.close();
        }
    }

    @Override
    public int getSize() {
        Jedis jedis = jedis();
        try {
            Map<String, String> map = jedis.hgetAll(mybatisKey);
            return map.size();
        } finally {
            //关闭链接
            jedis.close();
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
