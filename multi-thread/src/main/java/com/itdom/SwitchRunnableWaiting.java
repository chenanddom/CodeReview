package com.itdom;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.SwitchRunnableWaiting")
public class SwitchRunnableWaiting {

    private static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {

        new Thread(()->{
            synchronized (lock){
                log.debug("thread1 executing...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("execute other...");
            }

        },"t1").start();

        new Thread(()->{
            synchronized (lock){
                log.debug("thread2 executing...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("execute other...");
            }

        },"t2").start();

        Thread.sleep(2000);

        synchronized (lock){
            lock.notifyAll();
        }

    }

}
