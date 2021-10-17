package com.itdom;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;

/**
 * 批量重偏向 撤销20次之后就会开始优化转换成重偏向
 */
@Slf4j(topic = "c.TestBias")
public class TestBias3 {
    public static void main(String[] args) throws InterruptedException {
        Vector<Dog3> list = new Vector<>();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                Dog3 dog = new Dog3();
                list.add(dog);
                synchronized (dog) {
                    log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog).toPrintable());
                }
            }
            synchronized (list) {
                list.notify();
            }
        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            synchronized (list) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("-----------------------------------------------------------");
            for (int i = 0; i < 30; i++) {
                Dog3 dog3 = list.get(i);
                log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog3).toPrintable());
                synchronized (dog3) {
                    log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog3).toPrintable());
                }
                log.debug(i + "\t" + "{}", ClassLayout.parseInstance(dog3).toPrintable());
            }
        }, "t2");
        t2.start();
    }
}

class Dog3 {

}