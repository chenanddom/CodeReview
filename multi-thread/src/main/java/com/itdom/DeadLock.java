package com.itdom;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.DeadLock")
public class DeadLock {
    private static final Object A = new Object();
    private static final Object B = new Object();

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {

            synchronized (A) {
                log.debug("获得锁A，想要获取锁B");
                synchronized (B) {
                    log.debug("获得锁A，想要获取锁B...");
                }
            }
        }, "t1");
        thread.start();

        Thread thread1 = new Thread(() -> {

            synchronized (B) {
                log.debug("获得锁B，想要获取锁A");
                synchronized (A) {
                    log.debug("获得锁B，想要获取锁A");
                }

            }


        }, "t2");
        thread1.start();
    }
}
