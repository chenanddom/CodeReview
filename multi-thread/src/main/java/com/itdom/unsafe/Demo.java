package com.itdom.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Demo {
    static Unsafe unsafe;

    static {
        Field theUnsafe = null;
        try {
            theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//        theUnsafe.setAccessible(true);
//        Unsafe unsafe = (Unsafe)theUnsafe.get(null);
//        System.out.println(unsafe);

        Unsafe unsafe = getUnsafe();

        Field id = Student.class.getDeclaredField("id");
        Field name = Student.class.getDeclaredField("name");
        long idOffset = Demo.unsafe.objectFieldOffset(id);
        long nameOffset = Demo.unsafe.objectFieldOffset(name);

        Student student = new Student();

        unsafe.compareAndSwapInt(student,idOffset,0,20);
        unsafe.compareAndSwapObject(student,nameOffset,null,"zhangsan");

        System.out.println(student);
    }

    static Unsafe getUnsafe() {
        return unsafe;
    }

}

class Student {
    volatile int id;
    volatile String name;

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
