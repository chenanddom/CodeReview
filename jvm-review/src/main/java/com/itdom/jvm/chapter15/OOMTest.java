package com.itdom.jvm.chapter15;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OOMTest {
    public static void main(String[] args) {
        List<Person> personList = new ArrayList<Person>();
        for (int i = 0; i < 500; i++) {
            Person person = new Person("user:" + i, new byte[1024 * 100]);
            personList.add(person);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(new Scanner(System.in).next());
    }
}

class Person {
    private String username;
    private byte[] otherInfo;

    public Person(String username, byte[] otherInfo) {
        this.username = username;
        this.otherInfo = otherInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(byte[] otherInfo) {
        this.otherInfo = otherInfo;
    }
}
