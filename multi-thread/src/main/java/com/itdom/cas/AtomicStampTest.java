package com.itdom.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 *  运行的结果如下:
 *  11:12:35 [main] c.AtomicStampTest - the timeStamp:0
 * 11:12:35 [Thread-0] c.AtomicStampTest - the timeStamp:0,A->B:true
 * 11:12:35 [Thread-1] c.AtomicStampTest - the timeStamp:1,B>A:true
 * 11:12:36 [main] c.AtomicStampTest - the timeStamp:0,A->C:false
 */
@Slf4j(topic = "c.AtomicStampTest")
public class AtomicStampTest {


    public static AtomicStampedReference<String> reference = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {

        int stamp = reference.getStamp();
        log.debug("the timeStamp:{}", stamp);
        otherOperate();
        Thread.sleep(1000);
        log.debug("the timeStamp:{},A->C:{}", stamp, reference.compareAndSet(reference.getReference(), "C", stamp, stamp + 1));

    }

    public static void otherOperate() {
        new Thread(() -> {
            int stamp = reference.getStamp();
            log.debug("the timeStamp:{},A->B:{}", stamp, reference.compareAndSet(reference.getReference(), "C", stamp, stamp + 1));
        }).start();


        new Thread(() -> {
            int stamp = reference.getStamp();
            log.debug("the timeStamp:{},B>A:{}", stamp, reference.compareAndSet(reference.getReference(), "C", stamp, stamp + 1));
        }).start();
    }


}
