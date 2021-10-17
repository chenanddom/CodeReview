package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ReentrantLockConditionDemo")
public class ReentrantLockConditionDemo {

    private static final Object lock = new Object();
    private static boolean hasCigarette = false;//是否有烟
    private static boolean hashTakeout = false;//是否有外卖

    static ReentrantLock ROOM = new ReentrantLock();
    static Condition waitCigaretteSet = ROOM.newCondition();
    static Condition waitTakeoutSet = ROOM.newCondition();


    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            ROOM.lock();
            try {
                log.debug("有香烟没?[{}]", hasCigarette);
                while (!hasCigarette) {
                    try {
                        waitCigaretteSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有香烟没?[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("明白的干活");
                } else {
                    log.debug("没有香烟干不了活，先歇会!");
                }


            } finally {
                ROOM.unlock();
            }


        }, "小南").start();

        new Thread(() -> {
            ROOM.lock();
            try {
                log.debug("外卖到了没?[{}]", hashTakeout);
                while (!hashTakeout) {
                    log.debug("没有外卖干不了活，先歇会!");
                    try {
                        waitTakeoutSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("外卖到了没?[{}]", hashTakeout);
                if (hashTakeout) {
                    log.debug("明白的干活");
                } else {
                    log.debug("没有外卖干不了活，先歇会!");
                }
            } finally {
                ROOM.unlock();
            }


        }, "吃货").start();

        Thread.sleep(1000);
        new Thread(() -> {
            ROOM.lock();
            try {
                hasCigarette = true;
                waitCigaretteSet.signal();
            } finally {
                ROOM.unlock();
            }
        }, "送烟人").start();
        Thread.sleep(1000);
        new Thread(() -> {
            ROOM.lock();
            try {
                hashTakeout = true;
                waitTakeoutSet.signal();
            } finally {
                ROOM.unlock();
            }
        }, "送外卖").start();

    }

}
