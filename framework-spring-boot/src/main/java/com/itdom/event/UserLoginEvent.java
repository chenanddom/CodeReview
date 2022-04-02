package com.itdom.event;

import org.springframework.context.ApplicationEvent;

public class UserLoginEvent extends ApplicationEvent {
    private String username;
    public UserLoginEvent(Object source,String username) {
        super(source);
        this.username =username;
    }

    public String getUsername() {
        return username;
    }
}
