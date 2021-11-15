package com.itdom.guava.objectmethod;

import java.io.Serializable;

public class City implements Serializable {
    private String cityName;

    private Integer population;

    private Double averageRainFall;

    public City(String cityName, Integer population, Double averageRainFall) {
        this.cityName = cityName;
        this.population = population;
        this.averageRainFall = averageRainFall;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Double getAverageRainFall() {
        return averageRainFall;
    }

    public void setAverageRainFall(Double averageRainFall) {
        this.averageRainFall = averageRainFall;
    }
}
