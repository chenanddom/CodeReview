package com.itdom.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j(topic = "c.ThreadPoolShutdown")
public class ThreadPoolShutdown {
    public static void main(String[] args) throws InterruptedException {


        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(() -> {
            log.debug("1");
            Thread.sleep(2000L);
            return "1";}
            );
        executorService.submit(() -> {
            log.debug("2");
            Thread.sleep(500L);
            return "2";}
        );
        executorService.submit(() -> {
            log.debug("3");
            Thread.sleep(1000L);
            return "3";}
        );
//        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(() -> {
//            log.debug("1");
//            Thread.sleep(2000L);
//            return "1";
//        }, () -> {
//            log.debug("2");
//            Thread.sleep(500L);
//            return "2";
//        }, () -> {
//            log.debug("3");
//            Thread.sleep(400L);
//            return "3";
//        }));
        log.debug("shutdown");

        //不会接收新的任务
        executorService.shutdown();
        //shutdown()放啊调用后就无法添加新的任务
//        executorService.execute(() -> {
//            log.debug("追加一个新任务");
//        });

        //如果在线程池shutdown后还想等待,然后再做点别的事，可以使用awaitTermination
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        log.debug("other");




    }
}
