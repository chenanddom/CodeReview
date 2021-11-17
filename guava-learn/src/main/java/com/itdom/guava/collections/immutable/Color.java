package com.itdom.guava.collections.immutable;

public class Color {
    private String colorname;
    private int rgb;

    public Color() {
    }

    public Color(String colorname, int rgb) {
        this.colorname = colorname;
        this.rgb = rgb;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    public int getRgb() {
        return rgb;
    }

    public void setRgb(int rgb) {
        this.rgb = rgb;
    }
}
