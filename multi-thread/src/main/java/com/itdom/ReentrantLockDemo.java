package com.itdom;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock是可重入锁
 */
public class ReentrantLockDemo {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        m1();
    }

    public static void m1() {
        try {
            lock.lock();
            m2();
            System.out.println("----------1111111111111111--------------------");

        } finally {
            lock.unlock();
        }
    }

    public static void m2() {
        try {
            lock.lock();
            m3();
            System.out.println("----------2222222222222--------------------");
        } finally {
            lock.unlock();
        }
    }

    public static void m3() {
        System.out.println("----------3333333333333333--------------------");
    }

}
