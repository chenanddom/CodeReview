package com.itdom.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join 是 JDK 1.7 加入的新的线程池实现，它体现的是一种分治思想，适用于能够进行任务拆分的 cpu 密集型
 * 运算
 * 所谓的任务拆分，是将一个大任务拆分为算法上相同的小任务，直至不能拆分可以直接求解。跟递归相关的一些计
 * 算，如归并排序、斐波那契数列、都可以用分治思想进行求解
 * Fork/Join 在分治的基础上加入了多线程，可以把每个任务的分解和合并交给不同的线程来完成，进一步提升了运
 * 算效率
 * Fork/Join 默认会创建与 cpu 核心数大小相同的线程池
 * 2) 使用
 * 提交给 Fork/Join 线程池的任务需要继承 RecursiveTask（有返回值）或 RecursiveAction（没有返回值），例如下
 * 面定义了一个对 1~n 之间的整数求和的任务
 */
@Slf4j(topic = "c.ForkJoinThreadPoolTest")
public class ForkJoinThreadPoolTest {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        Integer result = forkJoinPool.invoke(new MyTask(5));
        log.debug("forkJoin result :{}",result);
//        System.out.println(result);
    }
}
@Slf4j(topic = "c.MyTask")
class MyTask extends RecursiveTask<Integer>{

    private  Integer num;
    public MyTask(int n) {
        this.num = n;
    }

    @Override
    protected Integer compute() {
        if (num<=0){
            return 0;
        }
        /**
         * 将任务拆分
         */
        MyTask myTask = new MyTask(this.num - 1);
        myTask.fork();
        /**
         * 合并结果
         */
        Integer ret = this.num + myTask.join();
        log.debug("n:{},result:{}",num,ret);
        return ret;
    }
}
