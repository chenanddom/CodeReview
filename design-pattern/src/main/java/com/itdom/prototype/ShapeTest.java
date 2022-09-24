package com.itdom.prototype;

public class ShapeTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        Shape triangle = new Triangle();
        triangle.show();
        System.out.println(triangle.hashCode());
        Object clone = ((Triangle) triangle).clone();
        System.out.println(clone.hashCode());

    }
}
