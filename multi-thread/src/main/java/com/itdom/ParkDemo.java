package com.itdom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ParkDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            LockSupport.park();
            System.out.println("unpark...");
            //将打断标记置为false
            System.out.println("打断状态:"+Thread.interrupted());
            LockSupport.park();
            System.out.println("unpark...");

        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
