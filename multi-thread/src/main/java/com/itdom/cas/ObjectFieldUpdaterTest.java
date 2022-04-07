package com.itdom.cas;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class ObjectFieldUpdaterTest {
    public static void main(String[] args) {
        AtomicReferenceFieldUpdater<Student, String> fieldUpdater = AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "username");
        Student student = new Student();
        System.out.println(fieldUpdater.compareAndSet(student, null, "张三"));

        System.out.println(student);

    }

}

class Student {
    volatile String username;

    public Student() {
    }

    public Student(String username) {
    this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Student{" +
                "username='" + username + '\'' +
                '}';
    }
}
