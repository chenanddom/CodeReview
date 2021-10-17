package com.itdom;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;
import java.util.concurrent.locks.LockSupport;

/**
 * 批量重偏向 撤销20次之后就会开始优化转换成重偏向
 */
@Slf4j(topic = "c.TestBias")
public class TestBias4 {
    static Thread t1,t2,t3;
    public static void main(String[] args) throws InterruptedException {
        Vector<Dog4> list = new Vector<>();
        t1 = new Thread(() -> {
            for (int i = 0; i < 39; i++) {
                Dog4 dog = new Dog4();
                list.add(dog);
                synchronized (dog) {
                    log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog).toPrintable());
                }
            }
            LockSupport.unpark(t2);
        }, "t1");
        t1.start();

        t2 = new Thread(() -> {
            LockSupport.park();
            log.debug("-----------------------------------------------------------");
            for (int i = 0; i < 39; i++) {
                Dog4 dog4 = list.get(i);
                log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog4).toPrintable());
                synchronized (dog4) {
                    log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog4).toPrintable());
                }
                log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog4).toPrintable());
            }
            LockSupport.unpark(t3);
        }, "t2");
        t2.start();



        t3 = new Thread(() -> {
            LockSupport.park();
            log.debug("-----------------------------------------------------------");
            for (int i = 0; i < 39; i++) {
                Dog4 dog4 = list.get(i);
                log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog4).toPrintable());
                synchronized (dog4) {
                    log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog4).toPrintable());
                }
                log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog4).toPrintable());
            }
        }, "t3");
        t3.start();
        t3.join();
        Dog4 dog4 = new Dog4();
        log.debug(1111 + "\t" + "{}", ClassLayout.parseInstance(dog4).toPrintable());
    }
}

class Dog4 {

}