package com.itdom.guava.cache;

public class Device {
private Long id;
private String sn;

    public Device() {
    }

    public Device(Long id, String sn) {
        this.id = id;
        this.sn = sn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
