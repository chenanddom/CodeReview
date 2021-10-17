package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock超时锁
 */
@Slf4j(topic = "c.ReentrantLockDemo2")
public class ReentrantLockTimeoutDemo {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                /**
                 * 如果没有竞争那么此方法会获取lock对象锁
                 * 如果有竞争进入阻塞队列
                 * 如果这个地方使用的是lock方法就无法实现打断的效果
                 */
                log.debug("t1 try get the lock");
                if (!lock.tryLock(1, TimeUnit.SECONDS)){
                    log.debug("t1 time out can not get the lock");
                    return;
                }
            } catch (InterruptedException e) {
                log.debug(" t1 can not get the lock");
                return;
            }
            try {
                log.debug(" get the lock");
            } finally {
                lock.unlock();
            }
        }, "t1");
        t1.start();
        lock.lock();
        log.debug("the main thread get the lock");
        log.debug(" the main thread hold lock");
        Thread.sleep(2000);
        lock.unlock();
    }


}
