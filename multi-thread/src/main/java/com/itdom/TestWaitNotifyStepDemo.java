package com.itdom;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用wait/notify实现分布的测试
 *
 */
@Slf4j(topic = "c.TestWaitNotifyStepDemo")
public class TestWaitNotifyStepDemo {
    private static final Object lock = new Object();
    private static boolean hasCigarette = false;//是否有烟
    private static boolean hashTakeout = false;


    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized (lock){
                log.debug("有香烟没?[{}]",hasCigarette);
                while (!hasCigarette){
                    log.debug("没有香烟干不了活，先歇会!");
                    try {
//                        Thread.sleep(2000);
                        lock.wait();//释放对象锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有香烟没?[{}]",hasCigarette);
                if (hasCigarette){
                    log.debug("明白的干活");
                }else {
                    log.debug("没有香烟干不了活，先歇会!");
                }
            }
        },"小南").start();

        new Thread(()->{
        synchronized (lock){
            log.debug("外卖到了没?[{}]",hashTakeout);
            while (!hashTakeout){
                log.debug("没有外卖干不了活，先歇会!");
                try {
//                        Thread.sleep(2000);
                    lock.wait();//释放对象锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("外卖到了没?[{}]",hashTakeout);
            if (hashTakeout){
                log.debug("明白的干活");
            }else {
                log.debug("没有外卖干不了活，先歇会!");
            }
        }
    },"吃货").start();


//        for (int i = 0; i < 5; i++) {
//            new Thread(()->{
//                synchronized (lock){
//                    log.debug("可以开始干活了");
//                }
//
//            },"其他人").start();
//        }

        Thread.sleep(1000);
        new Thread(()->{
            synchronized (lock) {
//                hasCigarette = true;
                hashTakeout=true;
                log.debug("外卖到咯");
                lock.notifyAll();//会错误唤醒
            }
        },"送外卖人").start();

    }
}
