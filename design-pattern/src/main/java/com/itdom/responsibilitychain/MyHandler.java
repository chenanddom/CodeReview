package com.itdom.responsibilitychain;

public class MyHandler extends AbstractHandler implements Handler{
    private String handlerName;

    public MyHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    public void operate() {
        System.out.println("handler:"+handlerName);
        if (getHandler()!=null){
            getHandler().operate();
        }
    }
}
