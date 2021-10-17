package com.itdom;

import lombok.extern.slf4j.Slf4j;

/**
 * sleep可以不和synchronized一起使用，wait一定要和synchronized一起使用
 * sleep是不会释放锁，wait是会释放锁的。
 */
@Slf4j(topic = "c.WaitSleepDemo")
public class WaitSleepDemo {
    private static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized (lock){
                log.debug("get lock 1");
                try {
                    Thread.sleep(20000L);
//                    lock.wait(20000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1").start();
            Thread.sleep(1L);
            log.debug("lock is release..");
            synchronized (lock){
                log.debug("get lock....");
            }
    }
}