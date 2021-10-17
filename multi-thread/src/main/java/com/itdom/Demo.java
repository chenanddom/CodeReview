package com.itdom;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Demo")
public class Demo {
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
//        Thread t = new Thread(() -> {
//            synchronized (lock) {
//                try {
//                    lock.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                log.debug("中断跳到这里");
//            }
//        });
//        t.start();
//
//        Thread.sleep(1000);
//        log.debug("开始中断当前线程中断线程");
//        t.interrupt();
//        log.debug("中断当前线程中断线程结束");
//

        for (int i = 0; i < 100; i++) {
            System.out.print("\r"+(i+1));
            Thread.sleep(100);
        }

    }
}
