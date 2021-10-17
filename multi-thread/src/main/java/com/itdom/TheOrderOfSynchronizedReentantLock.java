package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.itdom.SynchrousDemo.lock;

/**
 * 使用ReentrantLock实现线程顺序的的同步
 */
@Slf4j(topic = "c.TheOrderOfSynchronizedReentantLock")
public class TheOrderOfSynchronizedReentantLock {
    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();
        new Thread(()->{
            awaitSignal.print("a",a,b);
        },"t1").start();
        new Thread(()->{
            awaitSignal.print("b",b,c);
        },"t2").start();
        new Thread(()->{
            awaitSignal.print("c",c,a);
        },"t3").start();
        try {
            Thread.sleep(1000);
            awaitSignal.lock();
            try {
                //唤醒第一个
                log.debug("begin...");
              a.signal();
            }finally {
                awaitSignal.unlock();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class AwaitSignal extends ReentrantLock{
    private int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }
    public void print(String content, Condition current,Condition next){
        for (int i = 0; i < loopNumber; i++) {
                lock();
            try {
                current.await();
                System.out.print(content);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                unlock();
            }


        }
    }
}
