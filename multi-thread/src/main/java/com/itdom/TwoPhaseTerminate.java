package com.itdom;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用volatile实现两阶段结束
 * 1.可以使用中断的方式
 * 2.共有变量的可见性
 */
@Slf4j(topic = "c.TwoPhaseTerminate")
public class TwoPhaseTerminate {


    volatile static boolean stop=false;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {

//                if (stop){
//                    log.debug("thread is ending...");
//                    break;
//                }
//                try {
//                    Thread.sleep(1000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }



                Thread currentThread = Thread.currentThread();
//                如果线程被打断了就推出，但是如果线程在睡眠状态下被打断，那么线程的打断标记会被清除
                if (currentThread.isInterrupted()) {
                    log.debug("线程后事处理...."+currentThread.isInterrupted());
                    break;
                }

                try {
//                    如果线程在睡眠状态下被打断，那么线程的打断标记会被清除
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.debug("睡眠的线程被打断...."+currentThread.isInterrupted());
//                    睡眠状态下的线程被打断，线程标记会被清除
                    currentThread.interrupt();//打断标记被清除，需要重新设置
                }


            }

        }, "t1");
        t1.start();
//        log.debug("thread begin end....");
//        stop=true;

        log.debug("线程开始打断....");
        Thread.sleep(3500L);
        t1.interrupt();

    }



}
