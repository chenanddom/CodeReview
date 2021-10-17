package com.itdom.unsafe;


import sun.misc.Unsafe;
import java.util.ArrayList;
import java.util.List;

public class CustomerAtomicInterger implements Account{
    private volatile int value;
    private static final Unsafe UNSAFE;
    private static final long OFFSET;

    static {
        UNSAFE = Demo.getUnsafe();
        try {
            OFFSET = UNSAFE.objectFieldOffset(CustomerAtomicInterger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void decrement(int amount) {
        while (true) {
            int prev = this.value;
            int next = prev - amount;
            if (UNSAFE.compareAndSwapInt(this, OFFSET, prev, next)) {
                break;
            }
        }
    }

    @Override
    public Integer getBalance() {
        return this.value;
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }


    public CustomerAtomicInterger(int value) {
        this.value = value;
    }

    public static void main(String[] args) {
        Account.demo(new CustomerAtomicInterger(10000));
    }
}
interface Account {
    //获取余额
    Integer getBalance();

    //取款
    void withdraw(java.lang.Integer amount);
    /**
     * 方法内会启动1000个线程，每个线程做-10元的操作
     * 如果初始余额是10000那么正确的结果是0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance() + " cost: " + (end - start) / 1000_000 + "ms");
    }

}
