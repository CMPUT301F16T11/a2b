package com.cmput301f16t11.a2b;

import android.location.Address;

import java.io.Serializable;

/**
 * Created by brianofrim on 2016-11-09.
 */
public class Location implements Serializable {
   private Double lat;
   private Double lon;
   private String address;
    Location(Double newLat, Double newLon){
        lat = newLat;
        lon = newLon;
    }

    Location (Double newLat, Double newLon, String newAddress){
        lat = newLat;
        lon = newLon;
        address = newAddress;
    }

    public void setLat(Double newLat){
        lat = newLat;
    }
    public void setLon(Double newLon){
        lon = newLon;
    }

    //Getters
    public Double getLat(){
        return lat;
    }
    public Double getLon(){
        return lon;
    }
    public String getAddress(){return address;}

}
