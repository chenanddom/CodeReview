package com.itdom.factory;

public class GrateWallCarFactory implements CarFactory {
    public Car createCar() {
        return new GrateWallCar();
    }
}
