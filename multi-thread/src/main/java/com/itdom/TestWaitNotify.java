package com.itdom;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.TestWaitNotify")
public class TestWaitNotify {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {

            synchronized (lock) {
                log.debug("execution...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("execute other code...");
            }
        }, "t1").start();


        new Thread(() -> {

            synchronized (lock) {
                log.debug("execution...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("execute other code...");
            }
        }, "t2").start();

        Thread.sleep(2);
        synchronized (lock) {
            log.debug("notify other thread...");
            lock.notify();
        }
    }
}
