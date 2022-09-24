package com.itdom.builder;

public abstract class Builder {
    String productName;

    Double length;

    Double width;

    Double wight;
    public Builder setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public Builder setLength(Double length) {
        this.length = length;
        return this;

    }

    public Builder setWidth(Double width) {
        this.width = width;
        return this;

    }

    public Builder setWight(Double wight) {
        this.wight = wight;
        return this;
    }
    public abstract Product build();
}
