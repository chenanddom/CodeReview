package com.itdom.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 运行结果可以看到
 *
 2000000 cost:44
 2000000 cost:30
 2000000 cost:22
 2000000 cost:20
 2000000 cost:33
 ---------------------------------
 2000000 cost:25
 2000000 cost:22
 2000000 cost:17
 2000000 cost:13
 2000000 cost:12
 LongAddr比AtomicLong要快2-3倍
原因是：
 性能提升的原因很简单，就是在有竞争时，设置多个累加单元，Therad-0 累加 Cell[0]，而 Thread-1 累加
 Cell[1]... 最后将结果汇总。这样它们在累加时操作的不同的 Cell 变量，因此减少了 CAS 重试失败，从而提高性
 能。
 */
public class AtomicAccumulatorTest {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {

            demo(() -> new AtomicLong(),
                    (adder) -> adder.incrementAndGet());
        }
        System.out.println("---------------------------------");
        for (int i = 0; i < 5; i++) {

            demo(() -> new LongAdder(),
                    (adder) -> adder.increment());
        }
    }
    private static <T> void demo(
            Supplier<T> adderSupplier,
            Consumer<T> action
    ){
        T adder = adderSupplier.get();
        long start = System.nanoTime();
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(()->{
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
                
            }));
        }
            ts.forEach(e->e.start());

            ts.forEach(e->{
                try {
                    e.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });
        long end = System.nanoTime();
        System.out.println(adder+" cost:"+(end-start)/1000_000);

    }
}
