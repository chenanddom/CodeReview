package com.itdom.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;
@Slf4j(topic = "c.AtomicMarkableReferenceTest")
public class AtomicMarkableReferenceTest {
    public static void main(String[] args) throws InterruptedException {
        GarbageBad garbageBad = new GarbageBad("装满垃圾了");
        //参数关于mark可以看作一个标记，标识垃圾袋已经满了。
        AtomicMarkableReference<GarbageBad> ref = new AtomicMarkableReference<>(garbageBad, true);
        log.debug("start...");
        GarbageBad prev = ref.getReference();
        new Thread(()->{
            log.debug("start...");
            garbageBad.setDesc("空袋子");
            ref.compareAndSet(garbageBad,garbageBad,true,false);
            log.debug(garbageBad.toString());

        },"保洁阿姨").start();
        Thread.sleep(1000L);
        boolean success = ref.compareAndSet(prev, new GarbageBad("空垃圾袋"), true, false);
        log.debug("换袋子了吗？{}",success);
        log.debug(ref.getReference().toString());

    }
}

class GarbageBad {
    private String desc;

    public GarbageBad(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "GarbageBad{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
