package com.itdom;

/**
 * 守护线程会随着非守护进程的结束而结束。
 */
public class DeamonThread {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true){

            }
        });
        //设置为守护线程
        thread.setDaemon(true);
        thread.start();

        System.out.println(" main thread end ...");

    }
}
