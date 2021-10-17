package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.TheOrderOfSychronizedParkUnpark")
public class TheOrderOfSychronizedParkUnpark {
    private static Thread t1;
    private static Thread t2;
    private static Thread t3;

    public static void main(String[] args) {
        ParkUnpark parkUnpark = new ParkUnpark(5);
        t1 = new Thread(() -> {
            parkUnpark.print("a", t2);
        }, "t1");
        t2 = new Thread(() -> {
            parkUnpark.print("b", t3);
        }, "t2");
        t3 = new Thread(() -> {
            parkUnpark.print("c", t1);
        }, "t3");
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);

    }
}

class ParkUnpark {
    private int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String content, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(content);
            LockSupport.unpark(next);
        }
    }

}
