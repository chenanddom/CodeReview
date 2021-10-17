package com.itdom.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class TestAccount {
    public static void main(String[] args) {
        AccountUnsafe accountUnsafe = new AccountUnsafe((10000));
        Account.demo(accountUnsafe);
        AccountCas accountCas = new AccountCas(new AtomicInteger(10000));
        Account.demo(accountCas);
    }
}

/**
 * 使用CAS实现原子操作
 */
class AccountCas implements Account {

    private AtomicInteger balance;

    public AccountCas(AtomicInteger balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        return this.balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
//        while (true) {
//            //获取余额的最新值
//            int prev = balance.get();
//            int next = prev - amount;
//            if (balance.compareAndSet(prev, next)) {
//                break;
//            }
//        }
        this.balance.getAndAdd(-1*amount);
    }
}

/**
 * 使用sychronized
 */
class AccountUnsafe implements Account {


    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        synchronized (this) {
            return this.balance;
        }
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}


public interface Account {
    //获取余额
    Integer getBalance();

    //取款
    void withdraw(Integer amount);

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
