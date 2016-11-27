package com.cmput301f16t11.a2b;

import java.io.Serializable;

/**
 * Custom Location class for use in a2b project elasticsearch server
 */
public class Location implements Serializable {
    private Double lat;
    private Double lon;
    private String address;

    /**
     * Constructor for location obj
     *
     * @param newLat Latitude of the location (double)
     * @param newLon Longitude of the location (double)
     */
    Location(Double newLat, Double newLon){
        lat = newLat;
        lon = newLon;
    }

    /**
     * Constuctor for location obj that includes address string
     *
     * @param newLat latitude of the location
     * @param newLong longitude of the location
     * @param newAddress address of the location
     */
    Location(Double newLat, Double newLong, String newAddress) {
        this.lat = newLat;
        this.lon = newLong;
        this.address = newAddress;
    }

    /**
     * sets latitude
     *
     * @param newLat latitude value (double)
     */
    public void setLat(Double newLat){
        lat = newLat;
    }

    /**
     * sets longitude
     *
     * @param newLon longitude (double)
     */
    public void setLon(Double newLon){
        lon = newLon;
    }

    //Getters

    /**
     * gets the latitude
     *
     * @return current lat (double)
     */
    public Double getLat(){
        return lat;
    }

    /**
     * gets the longitude
     *
     * @return current long (double)
     */
    public Double getLon(){
        return lon;
    }

    /**
     * gets the address string
     *
     * @return address (string)
     */
    public String getAddress(){return address;}

}
