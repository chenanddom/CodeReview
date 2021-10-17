package com.itdom.factory;

public class GeelyCarFactory implements CarFactory {
    public Car createCar() {
        return new GeelyCar();
    }
}
