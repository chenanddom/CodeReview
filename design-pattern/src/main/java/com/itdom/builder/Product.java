package com.itdom.builder;

public class Product {

    private String productName;

    private Double length;

    private Double width;

    private Double wight;

    public Product(Builder builder) {
        this.productName = builder.productName;
        this.length = builder.length;
        this.width = builder.width;
        this.wight = builder.wight;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getWight() {
        return wight;
    }

    public void setWight(Double wight) {
        this.wight = wight;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", wight=" + wight +
                '}';
    }
}
