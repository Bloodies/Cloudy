package com.app.cloudy;

public class Clothing_Selection {
    public static double EffectiveTemperature(double temp, double rh, double ws){
        double e = rh/100 * 6.105 * Math.exp((17.27 * temp) / (237.7 + temp));
        return temp + 0.348 * e - 0.7 * ws - 4.25;
    }
}