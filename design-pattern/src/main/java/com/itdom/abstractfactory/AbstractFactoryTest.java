package com.itdom.abstractfactory;

public class AbstractFactoryTest {
    public static void main(String[] args) {
        AbstractFactory abstractFactory = new RealFactory();
        Screen screen = abstractFactory.createScreen();
        RemoteControl remoteControl = abstractFactory.createRemoteControl();
        screen.show();
        remoteControl.control();
    }
}
