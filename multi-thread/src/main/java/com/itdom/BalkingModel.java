package com.itdom;

import lombok.extern.slf4j.Slf4j;

/**
 * Balking模式
 */
@Slf4j(topic = "c.BalkingModel")
public class BalkingModel {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();
        twoPhaseTermination.start();
        Thread.sleep(5000L);
        twoPhaseTermination.stop();

    }

}
@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination{
    //监控线程
    private Thread monitorThread;
    //停止标记
    private volatile boolean stop = false;
    //判断是否执行过start();
    private volatile boolean starting = false;
    //启动监控线程
    public void start(){
        synchronized (this) {
            if (starting) {
                return;
            }
            starting = true;
        }
            monitorThread = new Thread(() -> {
                while (true) {

                    if (stop) {
                        log.debug("thread is ending...");
                        break;
                    }
                    try {
                        log.debug("Monitoring...");
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "monitor");
            monitorThread.start();
    }


    public void stop(){
        stop=true;
        monitorThread.interrupt();
    }

}
