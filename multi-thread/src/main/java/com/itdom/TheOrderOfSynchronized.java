package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 同步模式的顺序控制
 */
@Slf4j(topic = "c.TheOrderOfSynchronized")
public class TheOrderOfSynchronized {
    static Object obj = new Object();
    //标识t2线程是否已经运行了
    static boolean t2runed = false;


    public static void main(String[] args) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        try {
            new Thread(()->{});
        } finally {

        }


//----------------使用wait和notify控制顺序输出---------------------------
//        new Thread(() -> {
//            synchronized (obj) {
//                while (!t2runed) {
//                    try {
//                        obj.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                log.debug("1");
//
//            }
//        }, "t1").start();
//
//        new Thread(() -> {
//            synchronized (obj) {
//                t2runed = true;
//                obj.notify();
//                log.debug("2");
//            }
//        }, "t2").start();
//------------------------------使用park和unpark控制输出-------------------------------------

        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.debug("1");


        }, "t1");

        Thread t2 = new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1);

        }, "t2");
        t1.start();
        t2.start();


    }
}
