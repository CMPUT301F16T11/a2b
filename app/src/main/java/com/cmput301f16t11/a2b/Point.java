package com.cmput301f16t11.a2b;

/**
 * Created by brianofrim on 2016-11-09.
 */
public class Point {
    private Location location;
    Point(Double lat, Double lon){
        location = new Location(lat,lon);
    }


    // Setters
    public void setLat(Double newLat){
        location.setLat(newLat);
    }
    public void setLon(Double newLon){
        location.setLon(newLon);
    }

    //Getters
    public Double getLat(){
        return location.getLat();
    }
    public Double getLon(){
        return location.getLon();
    }

}
