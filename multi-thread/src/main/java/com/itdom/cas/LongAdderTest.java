package com.itdom.cas;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 执行结果：
 * 400000 cost:13
 * 400000 cost:12
 * 400000 cost:11
 * 400000 cost:14
 * 400000 cost:11
 * ----------------------------
 * 400000 cost:5
 * 400000 cost:3
 * 400000 cost:4
 * 400000 cost:3
 * 400000 cost:3
 *
 * 性能提升的原因很简单，就是在有竞争时，设置多个累加单元，Therad-0 累加 Cell[0]，而 Thread-1 累加
 * Cell[1]... 最后将结果汇总。这样它们在累加时操作的不同的 Cell 变量，因此减少了 CAS 重试失败，从而提高性
 * 能。
 */
public class LongAdderTest {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
        add(()->new AtomicInteger(0),(adder)->adder.getAndIncrement());
        }
        System.out.println("----------------------------");
        for (int i = 0; i < 5; i++) {

        add(()->new LongAdder(),(adder)->adder.increment());
        }
    }
    public static <T> void add(Supplier<T> supplier, Consumer<T> consumer){
        ArrayList<Thread> threads = new ArrayList<>();

        T v = supplier.get();
        for (int i = 0; i < 4; i++) {
            threads.add(new Thread(()->{
                for (int i1 = 0; i1 < 100000; i1++) {
                        consumer.accept(v);
                }
            }));
        }
        long start = System.nanoTime();
        threads.forEach(e->e.start());
        threads.forEach(e->{
            try {
                e.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(v + " cost:" + (end - start) / 1000_000);


    }

}


