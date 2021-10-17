package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock是可重入锁
 */
@Slf4j(topic = "c.ReentrantLockDemo2")
public class ReentrantLockDemo2 {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                /**
                 * 如果没有竞争那么此方法会获取lock对象锁
                 * 如果有竞争进入阻塞队列
                 * 如果这个地方使用的是lock方法就无法实现打断的效果
                 */
                log.debug("try get the lock");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                log.debug("can not get the lock");
                return;
            }
            try {
                log.debug(" get the lock");
            } finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock();
        t1.start();
        Thread.sleep(1000);
        log.debug("interrupt the t1");
        t1.interrupt();
    }


}
