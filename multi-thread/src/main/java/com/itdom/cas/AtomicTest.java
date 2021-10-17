package com.itdom.cas;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * 
 */
public class AtomicTest {
    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(5);
//        AtomicInteger i = new AtomicInteger(0);
//        System.out.println(i.incrementAndGet());//++i 1
//        System.out.println(i.getAndIncrement());//i++ 2
//        System.out.println(i.get());
        //接口  读取到 设置值
//        System.out.println(i.updateAndGet(x -> x * 5));
        updaAndGet(i,x->x*10);
        System.out.println(i.get());
    }

    public static void updaAndGet(AtomicInteger i, IntUnaryOperator operator) {
        int prev, next;
        do {
            prev = i.get();
            next = operator.applyAsInt(prev);
        } while (!i.compareAndSet(prev, next));
    }

}
