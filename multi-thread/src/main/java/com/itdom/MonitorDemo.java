package com.itdom;

/**
 * 监控线程
 */
public class MonitorDemo {
    public static void main(String[] args) throws InterruptedException {
        TwoPhasetermination twoPhasetermination = new TwoPhasetermination();
        twoPhasetermination.start();
        Thread.sleep(3500);
        twoPhasetermination.stop();


    }
}
class TwoPhasetermination{
    private Thread current;

    public void start(){
        current = new Thread(()->{
            while(true){
                if (current.isInterrupted()){
                    System.out.println("处理后事...");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    System.out.println("监控记录...");
                } catch (InterruptedException e) {
                    current.interrupt();
                }
            }
        });
        current.start();
    }

    public void stop(){
        current.interrupt();
    }

}
