package com.itdom.rabbitmq.workqueue;

public class SMS {
    private String username;
    private String mobile;
    private String message;

    public SMS(String username, String mobile, String message) {
        this.username = username;
        this.mobile = mobile;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
