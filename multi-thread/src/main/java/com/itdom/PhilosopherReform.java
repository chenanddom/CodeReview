package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家吃饭问题就会出现死锁问题，可以按照一定的顺序获取锁就可以解决死锁的问题，
 * 虽然这种按照顺序执行可以解决死锁问题，但是又会出现线程饥饿的问题，线程饥饿的问题
 * 需要使用ReentrantLock解决
 */
@Slf4j(topic = "c.Philosopher")
public class PhilosopherReform extends Thread {
    Chopstick2 left;
    Chopstick2 right;

    public PhilosopherReform(String name, Chopstick2 left, Chopstick2 right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
//        while (true) {
//            //先获取左手筷子
//            synchronized (left){
//                //获取右手筷子
//                synchronized (right){
//                    //调用吃饭方法
//                    eat();
//                }
//                //放下右手筷子
//            }
//            //放下左手筷子
//        }
        while (true){
            if (left.tryLock()){
                try {

                    if (right.tryLock()){

                        try{
                            eat();
                        }finally {
                            right.unlock();
                        }
                    }

                }finally {
                    //如果右边筷子获取不到就会释放锁
                    left.unlock();
                }
            }
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
        Chopstick2 c1 = new Chopstick2("1");
        Chopstick2 c2 = new Chopstick2("2");
        Chopstick2 c3 = new Chopstick2("3");
        Chopstick2 c4 = new Chopstick2("4");
        Chopstick2 c5 = new Chopstick2("5");

        new PhilosopherReform("p1",c1,c2).start();
        new PhilosopherReform("p2",c2,c3).start();
        new PhilosopherReform("p3",c3,c4).start();
        new PhilosopherReform("p4",c4,c5).start();
        new PhilosopherReform("p5",c5,c1).start();
    }
}

class Chopstick2 extends ReentrantLock{
    private String name;

    public Chopstick2(String name) {
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