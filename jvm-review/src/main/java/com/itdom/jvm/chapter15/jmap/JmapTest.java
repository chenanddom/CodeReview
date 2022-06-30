package com.itdom.jvm.chapter15.jmap;

import java.util.ArrayList;
import java.util.List;

public class JmapTest {
    public static void main(String[] args)  {
        List<byte[]> bytes = new ArrayList<byte[]>();

        for (int i = 0; i < 1000; i++) {
            bytes.add(new byte[1024 * 100]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }


}
