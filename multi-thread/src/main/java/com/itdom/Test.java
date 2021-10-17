package com.itdom;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test")
public class Test {

    public static void main(String[] args) throws InterruptedException {
//        Thread t = new Thread(() ->
//                log.debug("hello")
//        );
//        t.start();
//        log.debug("running");

        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        thread2.join();
//        System.out.println("thread2 end:"+System.currentTimeMillis());
        log.debug("thread2 end:"+System.currentTimeMillis());
        thread1.join();
//        System.out.println("thread1 end:"+System.currentTimeMillis());
        log.debug("thread1 end:"+System.currentTimeMillis());







    }

}
