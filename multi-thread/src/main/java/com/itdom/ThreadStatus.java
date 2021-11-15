package com.itdom;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.ThreadStatus")
public class ThreadStatus {
    public static void main(String[] args) {
        //new 状态
        Thread t1 = new Thread(() -> {
            log.debug("running...");
        }, "t1");
        //runnable
        Thread t2 = new Thread(() -> {
            while (true) {

            }

        }, "t2");

        t2.start();
        //teminated
        Thread t3 = new Thread(() -> {

            log.debug("running...");
        }, "t3");

        t3.start();

        //timed_waiting
        Thread t4 = new Thread(() -> {
            synchronized (ThreadStatus.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "t4");
        t4.start();

        //waiting 无限等待
        Thread t5 = new Thread(() -> {

            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t5");

        t5.start();

        /**
         * block
         */
        Thread t6 = new Thread(() -> {
            //无法获取得到锁
            synchronized (ThreadStatus.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t6");

        t6.start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("t1 state {}", t1.getState());
        log.debug("t2 state {}", t2.getState());
        log.debug("t3 state {}", t3.getState());
        log.debug("t4 state {}", t4.getState());
        log.debug("t5 state {}", t5.getState());
        log.debug("t6 state {}", t6.getState());


    }


}
