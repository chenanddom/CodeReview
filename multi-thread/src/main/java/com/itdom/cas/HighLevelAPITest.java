package com.itdom.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class HighLevelAPITest {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        int price=50;
        System.out.println(atomicInteger.updateAndGet(e -> {
            return e * 50;
        }));
        System.out.println(atomicInteger.get());
    }

}
