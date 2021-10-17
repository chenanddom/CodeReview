package com.itdom.cas;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicReferenceFieldUpdaterTest {
    public static void main(String[] args) {
        Student student = new Student();
        AtomicReferenceFieldUpdater<Student, String> newUpdater = AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");
        boolean flag = newUpdater.compareAndSet(student, null, "张三");
        System.out.println(flag);
        System.out.println(student.getName());
    }
}

class Student {
    public volatile String name;

    public Student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
