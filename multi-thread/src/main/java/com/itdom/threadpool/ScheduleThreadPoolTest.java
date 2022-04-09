package com.itdom.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

@Slf4j(topic = "c.ScheduleThreadPoolTest")
public class ScheduleThreadPoolTest {
    public static void main(String[] args) {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//            log.debug("test");
//            //Timer出错就无法执行
//            double i = 1/0;
//            }
//        },1000L,1000L);
        //线程池返回结果的时候会报错
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
//        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
//            log.debug("test...");
//            double i = 1 / 0;
//        }, 0, 1, TimeUnit.SECONDS);
//        try {
//            System.out.println(scheduledFuture.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        //实现周期的定实认为有
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);


        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        //每周五 14:51
        LocalDateTime time = now.withHour(14).withMinute(51).withSecond(0).withNano(0).with(DayOfWeek.FRIDAY);
        //如果当前时间以及超过了设置的时间那么就需要事下周的时间
        if (now.compareTo(time)>0){
            time = time.plusWeeks(1L);
        }
        System.out.println(time);
        long initialDelay = Duration.between(now,time).toMillis();

        long period = 1000*60*60*24*7;

        scheduledExecutorService.scheduleAtFixedRate(()->{
            log.debug("test....");
        },initialDelay,period,TimeUnit.MILLISECONDS);






    }

}
