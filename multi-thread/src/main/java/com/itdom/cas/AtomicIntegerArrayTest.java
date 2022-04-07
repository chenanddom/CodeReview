package com.itdom.cas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AtomicIntegerArrayTest {
    public static void main(String[] args) {

        demo(()->new int[10],(array)->array.length,(array,index)->array[index]++,(array)-> System.out.println(Arrays.toString(array)));

        demo(()->new AtomicIntegerArray(10),(array)->array.length(),(array, index)->array.getAndIncrement(index),(array)-> System.out.println(array));
    }
    public static <T> void demo(Supplier<T> supplier, Function<T,Integer> arrayLength, BiConsumer<T,Integer> biConsumer, Consumer<T> printConsumer){
        ArrayList<Thread> list = new ArrayList<>();
        T array = supplier.get();
        Integer length = arrayLength.apply(array);
        for (Integer i = 0; i < length; i++) {
            list.add(new Thread(()->{
                for (int i1 = 0; i1 < 10000; i1++) {
                    biConsumer.accept(array,i1%length);
                }
            }));
        }
        list.forEach(e->e.start());
        list.forEach(e->{
            try {
                e.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        printConsumer.accept(array);
    }
}
