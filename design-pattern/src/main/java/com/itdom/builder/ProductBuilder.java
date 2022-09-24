package com.itdom.builder;

public class ProductBuilder extends Builder {

    public static Builder builder() {
        return new ProductBuilder();
    }

    public Product build() {
        return new Product(this);
    }
}
