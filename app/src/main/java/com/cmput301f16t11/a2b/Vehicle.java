package com.cmput301f16t11.a2b;

/**
 * Created by toth on 11/18/2016.
 */

public class Vehicle {
    private static String make;
    private static String model;
    private static String color;
    private static int year;

    public Vehicle(){
        make = "";
        model = "";
        color = "";
        year = 0;

    }

    public Vehicle(String make, String model, String color, int year){
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;

    }


    public static String getMake() {
        return make;
    }

    public static void setMake(String make) {
        Vehicle.make = make;
    }

    public static String getModel() {
        return model;
    }

    public static void setModel(String model) {
        Vehicle.model = model;
    }

    public static String getColor() {
        return color;
    }

    public static void setColor(String color) {
        Vehicle.color = color;
    }

    public static int getYear() {
        return year;
    }

    public static void setYear(int year) {
        Vehicle.year = year;
    }
}
