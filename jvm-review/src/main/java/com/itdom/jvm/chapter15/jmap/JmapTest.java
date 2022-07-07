package com.itdom.jvm.chapter15.jmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JmapTest {
    public static void main(String[] args)  {
        List<byte[]> bytes = new ArrayList<byte[]>();

        for (int i = 0; i < 500; i++) {
            bytes.add(new byte[1024 * 100]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println(new Scanner(System.in).next());

    }


}
