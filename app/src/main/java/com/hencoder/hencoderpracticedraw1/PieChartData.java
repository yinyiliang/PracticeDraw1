package com.hencoder.hencoderpracticedraw1;

/**
 * Created by yyl on 2017/11/27.
 */

public class PieChartData {
    private String name;
    private float number;
    private int color;

    public PieChartData(String name, float number, int color) {
        this.name = name;
        this.number = number;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public float getNumber() {
        return number;
    }

    public int getColor() {
        return color;
    }
}
