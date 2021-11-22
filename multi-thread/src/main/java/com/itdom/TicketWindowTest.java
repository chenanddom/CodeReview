package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
@Slf4j(topic = "c.TestBias")
public class TicketWindowTest {
    private static Random random = new Random();
    /**
     * 余票的数量
     */
    private Integer count;

    public TicketWindowTest(Integer count) {
        this.count = count;
    }
    /**
     *抢票操作
     * @param num 抢票的数量
     */
    public synchronized int ticketGrabbing(Integer num){
        if (num==null){
            return 0;
        }
        if (this.count>num){
            this.count-=num;
            return num;
        }else{
            return 0;
        }
    }
    public static int randomAmount(){
        return random.nextInt(5)+1;
    }

    public Integer getCount() {
        return count;
    }

    public static void main(String[] args) {
        TicketWindowTest ticketWindowTest = new TicketWindowTest(1000);
        List<Thread> threadList = new ArrayList<>();
        List<Integer> ticketNumbers = Collections.synchronizedList(new ArrayList<Integer>());
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                ticketNumbers.add(ticketWindowTest.ticketGrabbing(randomAmount()));
            });
            threadList.add(thread);
               thread.start();
        }
        threadList.forEach(e->{
            try {
                e.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        log.debug("余票:{}",ticketWindowTest.getCount());
        int sum = ticketNumbers.parallelStream().mapToInt(Integer::intValue).sum();
        log.debug("出售的票数量:{}",sum);
    }
}
