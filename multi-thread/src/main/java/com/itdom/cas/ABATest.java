package com.itdom.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题的测试，需要使用AtomicStampReference来解决这个问题。
 *
 */
@Slf4j(topic = "c.ABATest")
public class ABATest {

//    static AtomicReference<String> ref = new AtomicReference<>("A");
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A",0);
    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
//        String prev = ref.get();
        //获取共享变量的当前值
        other();
        String prev = ref.getReference();
        int stamp = ref.getStamp();
        log.debug("{}",stamp);
        Thread.sleep(1000L);
        log.debug("change A->C {}",ref.compareAndSet(prev,"C",stamp,stamp+1));
    }

    private static void other() throws InterruptedException {
        new Thread(()->{
            String prev = ref.getReference();
            int stamp = ref.getStamp();
            log.debug("change A->B {}",ref.compareAndSet(prev,"B",stamp,stamp+1));

        },"t1").start();
        Thread.sleep(500);
        new Thread(()->{
            String prev = ref.getReference();
            int stamp = ref.getStamp();
            log.debug("change B->A {}",ref.compareAndSet(prev,"A",stamp,stamp+1));
        },"t2").start();


    }
}
