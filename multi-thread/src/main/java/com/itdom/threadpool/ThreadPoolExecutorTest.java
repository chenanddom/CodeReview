package com.itdom.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j(topic = "c.ThreadPoolExecutorTest")
public class ThreadPoolExecutorTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        /**
         * LinkedBlockingQueue没有指定队列的大小，是无界队列,可以放任意数量的任务
         * 适用于任务量已知，相对耗时的任务
         *
         */
//        ExecutorService service = Executors.newFixedThreadPool(2);
//        service.execute(()->{log.debug("1"); });
//        service.execute(()->{log.debug("2"); });
//        service.execute(()->{log.debug("3"); });
        /**
         * 核心线程数是0.最大线程数是Integer.MAX_VALUE,救急线程的空闲生存时间是60s，意味者
         *      1. 全部是救急线程(60s后回收)
         *      2. 救急咸亨可以无线的创建
         *  队列采用的使用SynchronousQueue实现特点是，它没有容量，没有线程来去是放不进去的(意味着有取才能放)
         *  整个线程池表现为线程数会根据任务量不断增长，没有上限，当任务执行完毕，空闲 1分钟后释放线
         * 程。 适合任务数比较密集，但每个任务执行时间较短的情况
         */
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
//        new Thread(()->{
//            try {
//            log.debug("putting {}",1);
//                queue.put(1);
//            log.debug("putted {}",1);
//            log.debug("putting {}",2);
//                queue.put(2);
//                log.debug("putted {}",2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        },"t1").start();
//
//        Thread.sleep(1000L);
//
//        new Thread(()->{
//            try {
//                log.debug("taking {}",1);
//                queue.take();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        },"t2").start();
//
//
//        Thread.sleep(1000L);
//        new Thread(()->{
//            try {
//                log.debug("taking {}",2);
//                queue.take();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        },"t3").start();

        /**
         * 获取返回的结果
         */
        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        Future<String> future = executorService.submit(() -> {
//            log.debug("future接收返回结果");
//            return "OK";
//        });
//        System.out.println(future.get());

// 提交 tasks 中所有任务
        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(() -> {
            log.debug("1");
            Thread.sleep(1000L);
            return "1";
        }, () -> {
            log.debug("2");
            Thread.sleep(500L);
            return "2";
        }, () -> {
            log.debug("3");
            Thread.sleep(400L);
            return "3";
        }));
        futures.forEach(e-> {
            try {
                System.out.println(e.get());
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ExecutionException executionException) {
                executionException.printStackTrace();
            }
        });
        // 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
        String ret = executorService.invokeAny(Arrays.asList(() -> {
            log.debug("1");
            Thread.sleep(1000L);
            return "1";
        }, () -> {
            log.debug("2");
            Thread.sleep(500L);
            return "2";
        }, () -> {
            log.debug("3");
            Thread.sleep(400L);
            return "3";
        }));
        System.out.println(ret);


    }

}
