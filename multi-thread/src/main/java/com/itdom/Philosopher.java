package com.itdom;

import lombok.extern.slf4j.Slf4j;

/**
 * 哲学家吃饭问题就会出现死锁问题，可以按照一定的顺序获取锁就可以解决死锁的问题，
 * 虽然这种按照顺序执行可以解决死锁问题，但是又会出现线程饥饿的问题，线程饥饿的问题
 * 需要使用ReentrantLock解决
 */
@Slf4j(topic = "c.Philosopher")
public class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            //先获取左手筷子
            synchronized (left){
                //获取右手筷子
                synchronized (right){
                    //调用吃饭方法
                    eat();
                }
                //放下右手筷子
            }
            //放下左手筷子
        }
    }
    private void eat() {
        log.debug("eating...");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");

        new Philosopher("p1",c1,c2).start();
        new Philosopher("p2",c2,c3).start();
        new Philosopher("p3",c3,c4).start();
        new Philosopher("p4",c4,c5).start();
        new Philosopher("p5",c1,c5).start();
    }
}

class Chopstick {
    private String name;

    public Chopstick(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chopstick{" +
                "name='" + name + '\'' +
                '}';
    }
}