package com.itdom.builder;

public class Direct {


    public static void main(String[] args) {
        Product product = ProductBuilder.builder().setProductName("test1").setLength(1.00).setWidth(1.00).setWight(10.00).build();
        System.out.println(product);
    }

}
