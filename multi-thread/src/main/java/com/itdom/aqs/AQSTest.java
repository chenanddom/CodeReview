package com.itdom.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j(topic = "c.AQSTest")
public class AQSTest {
    public static void main(String[] args) {

        MyLock myLock = new MyLock();
        new Thread(()->{
            myLock.lock();
            log.debug("locking...");
            myLock.lock();
            try {
                log.debug("locking...");
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug("unlocking...");
                myLock.unlock();
            }
        },"t1").start();
        new Thread(()->{
            myLock.lock();
            try {
                log.debug("locking...");
            }finally {
                log.debug("unlocking...");
                myLock.unlock();
            }
        },"t2").start();
    }
}

/**
 * 不可重入锁
 */
class MyLock implements Lock {

    class MySync extends AbstractQueuedLongSynchronizer {
        @Override
        protected boolean  tryAcquire(long arg) {
            if (compareAndSetState(0, 1)) {
                //加上了锁，并且设置owner为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(long arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        /**
         * 是否持有独占锁
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState()==1;

        }
        public Condition newCondition(){
        return new ConditionObject();
        }
    }

    private MySync sync = new MySync();

    /**
     * 加锁，如果加锁失败就会加入队列
     */
    @Override
    public void lock() {
        sync.acquire(1);

    }

    /**
     * 加锁，可打断
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 尝试加锁(指挥尝试一次)
     * @return
     */
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    /**
     * 带超时时间的尝试加锁
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    /**
     * 解锁
     */
    @Override
    public void unlock() {
        sync.release(1);
    }

    /**
     * 条件
     * @return
     */
    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}