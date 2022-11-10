package com.itdom.influxdb;

import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.function.*;
import java.util.stream.Stream;

public class Demo {
    public static void main(String[] args) throws InterruptedException {

//        Set<String> set = new HashSet<String>();
//        set.add("1");
//        set.add("2");
//        set.add("a");
//        set.add("3");
//        Iterator<String> iterator = set.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();
//        new Thread(() -> {
//            try {
//                synchronousQueue.put("a");
//                System.out.println(Thread.currentThread().getName()+" put a ");
//                synchronousQueue.put("b");
//                System.out.println(Thread.currentThread().getName()+" put b ");
//                synchronousQueue.put("c");
//                System.out.println(Thread.currentThread().getName()+" put c ");
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "putThread").start();
//        new Thread(() -> {
//            try {
//                System.out.println("get:"+synchronousQueue.take());
//                System.out.println("get:"+synchronousQueue.take());
//                System.out.println("get:"+synchronousQueue.take());
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "takeThread").start();


//        Supplier<String> supplier = ()->"THIS IS A TEST";
//        System.out.println(supplier.get());
//
//        Consumer<String> consumer = (k)-> System.out.println(k);
//        consumer.accept("CONSUMER");
//
//        Function<String,String> function = (t)->t.toUpperCase();
//        System.out.println(function.apply("test demo"));
//
//
//        UnaryOperator<Integer> unaryOperator = i->i*i;
//        System.out.println(unaryOperator.apply(9));
//
//        BiFunction<Integer,Integer,String> biFunction = (k,v)->k+"*"+v+"="+k*v;
//        System.out.println(biFunction.apply(9,9));

///////////////-------------------流-------------------------------------------------------///////////////
/*String[] arr = {"hello","ok","demo"};
        //数组的方式创建流
        System.out.println("--------------------数组的方式创建流--------------------------");
        Arrays.stream(arr).forEach(System.out::println);
        //List的方式创建流
        System.out.println("--------------------List的方式创建流--------------------------");
        Arrays.asList(arr).stream().forEach(System.out::println);
        //Stream.of()的方式
        System.out.println("--------------------Stream.of()的方式--------------------------");
        Stream.of(arr).forEach(System.out::println);
        System.out.println("--------------------迭代器方式--------------------------");
        //迭代器
        Stream.iterate(1,i->i+1).limit(100).forEach(System.out::println);
        System.out.println("--------------------generate方式--------------------------");
        Stream.generate(()->new Random().nextInt(10)).limit(10).forEach(System.out::println);*/

///////--------------------------------JDK8 流编程的完整案例 元素的中间操作与终止操作 -------------------------------////////////
        String[] arr = {"react", "", "spring", "bo_le", "webflux", "spring", "bo_le"};
        Stream.of(arr)
                .filter(i->!i.isEmpty())
//                .peek(i-> System.out.println(i))//peek是一个中间操作，必须要要配合终止操作才可以
                .sorted()
//                .findFirst()//此处是一个终止操作，流编程中终止操作可以有多个，终止操作可以有多个
                .limit(1)
                .map(i->i.replace("_",""))
                .flatMap(i->Stream.of(i.split("")))//将字符串组成新的流
                .forEach(System.out::println);

    }
}
