package com.itdom;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * 锁消除优化，在没有线程竞争的情况下JVM会优化为synchronized的对象锁进行
 * 优化，实现对对象锁的消除。-XX:-EliminateLocks
 * -XX:-EliminateLocks
 */
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations=3)
@Measurement(iterations=5)
public class MyBenchmark {

    static int x = 0;

    @Benchmark
    public void a() {
        x++;
    }

    @Benchmark
    public void b() {
        Object o = new Object();
        synchronized (o) {
            x++;
        }
    }


}
