package com.itdom.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ThreadPooltTest")
public class ThreadPooltTest {
    public static void main(String[] args) {
        ThreadPooltTest threadPooltTest = new ThreadPooltTest(1, 1000, TimeUnit.MILLISECONDS, 1,
                //死等
//                ((queue, task) -> queue.put(task))
                //带超时时间的等待
//                (queue, task) -> queue.offer(task,500,TimeUnit.MILLISECONDS)
                //放弃
//                (queue, task) ->log.debug("放弃...")
               //抛出异常
//                (queue, task) ->{throw new RuntimeException("抛弃任务:"+task);}
                //自己处理
                (queue, task) -> {task.run();}
        );
        for (int i = 0; i < 15; i++) {
            int j = i;
            threadPooltTest.execute(() -> {
//                log.debug("{}", j);
                try {
                    Thread.sleep(1000L);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("{}", j);
            });
        }
    }

    // 任务队列
    private BlockingQueue<Runnable> taskQueue;
    //线程集合
    private HashSet<Worker> workers = new HashSet<>();
    //核心线程数
    private int coreSize;
    //获取任务的超时时间
    private long timeout;
    private TimeUnit timeUnit;

    private RejectPolicy rejectPolicy;

    public ThreadPooltTest(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity,RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy=rejectPolicy;
    }

    //执行任务
    public void execute(Runnable task) {
        //当任务数没有超过coreSize时，直接交给worker对象执行
        //如果任务超过了coreSize，加入任务队列暂存
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增worker{}", worker);
                workers.add(worker);
                worker.start();
            } else {
//                log.debug("加入任务队列{}", task);
//                taskQueue.put(task);
                /*
                核心线程使用完之后有以下几种可能可以选择
                    1. 死等
                    2. 带超时时间等待
                    3. 绕过调用者放弃任务执行
                    4. 让调用者自己执行任务
                    5. 让调用者抛出异常
                 */
                taskQueue.tryPut(rejectPolicy,task);
            }
        }
    }


    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable target) {
            this.task = target;
        }

        @Override
        public void run() {
            //执行任务
            //1. 当task不为空，执行任务
            //2. 当task任务执行完毕，接着从队列获取任务继续执行
//            while (task != null || (task = taskQueue.take()) != null) {
            while (task != null || (task = taskQueue.poll(timeout,TimeUnit.MILLISECONDS)) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("Worker被移除{}", this);
                workers.remove(task);
            }
        }
    }


}
@FunctionalInterface
interface RejectPolicy<T>{
    void reject(BlockingQueue<T> queue,T task);
}

@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T> {
    //1.任务队列
    private Deque<T> queue = new ArrayDeque<>();
    //2. 锁
    private ReentrantLock lock = new ReentrantLock();
    //3. 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //4. 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();
    //5.容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    //带超市的等待
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();

        try {
            //将时间转换称纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    if (nanos < 0) {
                        return null;
                    }
                    //返回是剩余时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            //此处要通知生产者
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }

    }


    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            //此处要通知生产者
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }


    }

    /**
     * 带超时时间的添加队列的方法
     *
      * @return
     */
    public  boolean offer(T task,long timeout,TimeUnit unit){
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while (queue.size() == capacity) {
                try {
                    if (nanos<0){
                        return false;
                    }
                    log.debug("等待加入队列:{}",task);
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("任务加入队列:{}",task);
            queue.addLast(task);
            //通知消费者
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }


    public void put(T task) {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入队列:{}",task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("任务加入队列:{}",task);
            queue.addLast(task);
            //通知消费者
            fullWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return capacity;
        } finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy rejectPolicy, T task) {
    lock.lock();
    try {
        //判断队列是否满了
        if (queue.size()==capacity){
            rejectPolicy.reject(this,task);
        }else {
        //队列有空闲
        log.debug("任务加入队列{}",task);
        queue.addLast(task);
        emptyWaitSet.signal();
        }

    }finally {
        lock.unlock();
    }

    }
}
