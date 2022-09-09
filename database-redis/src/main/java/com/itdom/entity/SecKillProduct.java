package com.itdom.entity;

import java.io.Serializable;

public class SecKillProduct implements Serializable {
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品编号
     */
    private String productNumber;

    private String userId;

    public SecKillProduct() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
