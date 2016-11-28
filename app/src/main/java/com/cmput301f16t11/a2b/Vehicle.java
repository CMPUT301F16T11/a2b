package com.cmput301f16t11.a2b;

/**
 * Created by toth on 11/18/2016.
 */

public class Vehicle {
    private String make;
    private String model;
    private String color;
    private int year;

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

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
