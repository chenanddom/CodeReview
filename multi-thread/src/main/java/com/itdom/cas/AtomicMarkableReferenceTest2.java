package com.itdom.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;
@Slf4j(topic = "c.AtomicMarkableReferenceTest2")
public class AtomicMarkableReferenceTest2 {

    public static void main(String[] args) throws InterruptedException {
        GarbageBag bag = new GarbageBag("装满了垃圾");
        AtomicMarkableReference<GarbageBag> reference = new AtomicMarkableReference<>(bag, true);
        log.debug("main thread start...");
        GarbageBag prev = reference.getReference();
        log.debug(prev.toString());

        new Thread(()->{
        log.debug("打扫卫生的线程start....");
        bag.setDesc("空垃圾袋");
        while (!reference.compareAndSet(bag, new GarbageBag("空垃圾袋"),true,false)){}
        log.debug(bag.toString());

        },"保姆线程").start();

        Thread.sleep(1000);
        log.debug("主线程想换一只新垃圾袋？");
        boolean success = reference.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);
        log.debug("换了么？" + success);
        log.debug(reference.getReference().toString());
    }

}

class GarbageBag {
    String desc;

    public GarbageBag(String desc) {
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return super.toString() + " " + desc;
    }
}