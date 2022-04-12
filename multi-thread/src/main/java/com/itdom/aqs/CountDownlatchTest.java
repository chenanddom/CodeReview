package com.itdom.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
@Slf4j(topic = "c.CountDownLatch")
public class CountDownlatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);

            new Thread(()->{
                try {
                    log.debug("begin...");
                    Thread.sleep(1000L);
                    countDownLatch.countDown();
                    log.debug("end...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            },"t1").start();

        new Thread(()->{
            try {
                log.debug("begin...");
                Thread.sleep(1000L);
                countDownLatch.countDown();
                log.debug("end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t2").start();

        new Thread(()->{
            try {
                log.debug("begin...");
                Thread.sleep(1000L);
                countDownLatch.countDown();
                log.debug("end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t3").start();

        log.debug("main thread waitting....");
        countDownLatch.await();
        log.debug("main thread waitting end...");

    }
}
