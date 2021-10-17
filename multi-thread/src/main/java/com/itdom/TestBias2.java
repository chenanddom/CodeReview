package com.itdom;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 有其他线程对象使用偏向锁就会膨胀成为轻量级锁。
 */
@Slf4j(topic = "c.TestBias")
public class TestBias2 {
    public static void main(String[] args) throws InterruptedException {

        Dog2 dog2 = new Dog2();

        new Thread(() -> {
            log.debug(ClassLayout.parseInstance(dog2).toPrintable());
            synchronized (dog2) {
                log.debug(ClassLayout.parseInstance(dog2).toPrintable());
            }
            log.debug(ClassLayout.parseInstance(dog2).toPrintable());
            synchronized (TestBias2.class) {
                TestBias2.class.notify();
            }
        }, "t1").start();
        log.debug("-----------------------------------------------------------------");

        new Thread(() -> {
            synchronized (TestBias2.class) {
                try {
                    TestBias2.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug(ClassLayout.parseInstance(dog2).toPrintable());
            synchronized (dog2) {
                log.debug(ClassLayout.parseInstance(dog2).toPrintable());
            }
            log.debug(ClassLayout.parseInstance(dog2).toPrintable());


        }, "t2").start();

    }
}

class Dog2 {

}