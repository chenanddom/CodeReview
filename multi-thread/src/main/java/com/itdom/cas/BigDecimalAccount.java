package com.itdom.cas;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j(topic = "c.BigDecimalAccountTest")
class BigDecimalAccountTest {
    public static void main(String[] args) {
        BigDecimalAccount.demo(new DecimalAccountCas(new BigDecimal("10000")));
    }
}

class DecimalAccountCas implements BigDecimalAccount {
    private AtomicReference<BigDecimal> balance;

    public DecimalAccountCas(BigDecimal balance) {
        this.balance = new AtomicReference<BigDecimal>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return this.balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        BigDecimal prev, next;
            do {
                prev = balance.get();
                next = prev.subtract(amount);
            } while (!this.balance.compareAndSet(prev, next));
    }
}

public interface BigDecimalAccount {
    //获取余额
    BigDecimal getBalance();

    //取款
    void withdraw(BigDecimal amount);

    /**
     * 方法内会启动1000个线程，每个线程做-10元的操作
     * 如果初始余额是10000那么正确的结果是0
     */
    public static void demo(BigDecimalAccount account) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
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
