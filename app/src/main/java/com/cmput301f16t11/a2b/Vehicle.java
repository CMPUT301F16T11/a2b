package com.cmput301f16t11.a2b;

/**
 * Created by toth on 11/18/2016.
 */
public class Vehicle {
    private String make;
    private String model;
    private String color;
    private int year;

    /**
     * Instantiates a new Vehicle.
     */
    public Vehicle(){
        make = "";
        model = "";
        color = "";
        year = 0;
    }

    /**
     * Instantiates a new Vehicle.
     *
     * @param make  the make
     * @param model the model
     * @param color the color
     * @param year  the year
     */
    public Vehicle(String make, String model, String color, int year){
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
    }


    /**
     * Is set boolean.
     *
     * @return the boolean
     */
    public Boolean isSet(){

        if(make.isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * Gets make.
     *
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets make.
     *
     * @param make the make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets year.
     *
     * @param year the year
     */
    public void setYear(int year) {
        this.year = year;
    }
}
