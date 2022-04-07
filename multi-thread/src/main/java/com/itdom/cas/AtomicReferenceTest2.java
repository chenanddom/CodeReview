package com.itdom.cas;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest2 implements BigDecimalAccount{
    public static void main(String[] args) {
        BigDecimalAccount.demo(new AtomicReferenceTest2(new BigDecimal(10000)));
    }
    private AtomicReference<BigDecimal> balance;

    public AtomicReferenceTest2(BigDecimal balance) {
        this.balance = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while (true){
            BigDecimal prev = balance.get();
            BigDecimal nextv = prev.subtract(amount);
            if (balance.compareAndSet(prev,nextv)){
                break;
            }
        }
    }
}
