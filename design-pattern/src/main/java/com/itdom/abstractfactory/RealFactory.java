package com.itdom.abstractfactory;

public class RealFactory implements AbstractFactory{

    public Screen createScreen() {
        return new AocScreen();
    }

    public RemoteControl createRemoteControl() {
        return new HpRemoteControl();
    }
}
