package com.itdom;

import lombok.extern.slf4j.Slf4j;

/**
 * 同步顺序控制之交替输出
 */
@Slf4j(topic = "c.TheOrderOfSynchronizedOutputAlternately")
public class TheOrderOfSynchronizedOutputAlternately {
    public static void main(String[] args) {
        SynWaitNotify synWaitNotify = new SynWaitNotify(1, 5);
        new Thread(()->{
            synWaitNotify.print(1,2,"a");

        },"t1").start();
        new Thread(()->{
            synWaitNotify.print(2,3,"b");

        },"t2").start();
        new Thread(()->{
            synWaitNotify.print(3,1,"c");


        },"t3").start();
    }
}

class SynWaitNotify {

    private int flag;
    private int loopNumber;

    public SynWaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(int waitFlag, int nextFlag, String content) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (this.flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(content);
                flag = nextFlag;
                this.notifyAll();
            }
        }


    }


}







