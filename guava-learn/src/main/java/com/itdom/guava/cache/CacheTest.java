package com.itdom.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheTest {

    public static void test1() throws ExecutionException, InterruptedException {
        LoadingCache<String, Device> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(1000, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, Device>() {
                    @Override
                    public Device load(String s) throws Exception {
                        System.out.println("new create...");
                        return new Device(1L, "sdaew1223221");
                    }
                });
        System.out.println(loadingCache.getUnchecked("1L").getSn());
        Thread.sleep(1500L);//让缓存过期
        System.out.println(loadingCache.getUnchecked("1L").getSn());
    }

    /**
     * 刷新缓存
     */
    public static void test2() throws InterruptedException {
        LoadingCache<String, Device> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .refreshAfterWrite(1000, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, Device>() {

                    @Override
                    public Device load(String s) throws Exception {
                        System.out.println("new create...");
                        return new Device(1L, "23384322");
                    }

                    @Override
                    public ListenableFuture<Device> reload(String key, Device oldValue) throws Exception {
                        oldValue.setId(1L);
                        oldValue.setSn(UUID.randomUUID().toString());
                        return Futures.immediateFuture(oldValue);
                    }
                });
        System.out.println(loadingCache.getUnchecked("1L").getSn());
        Thread.sleep(1500L);//让缓存过期
        System.out.println(loadingCache.getUnchecked("1L").getSn());


    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        CacheTest.test1();
        CacheTest.test2();
    }
}
