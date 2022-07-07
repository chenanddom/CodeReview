package com.itdom.volatiletest;

public class VolatileTest {

    private static volatile boolean initFlag = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println("waiting data...");

            while (!initFlag) {

            }
            System.out.println("get data success!!!");
        }, "t1").start();
        Thread.sleep(2000L);
        new Thread(() -> {
            otherOperate();
        }, "t2").start();
    }

    public static void otherOperate() {
        System.out.println("prepare modify the initFlag variables");
        initFlag = true;
        System.out.println("modify the initFlag variables");
    }
}
