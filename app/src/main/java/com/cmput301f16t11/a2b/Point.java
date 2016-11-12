package com.cmput301f16t11.a2b;

/**
 * A custom class for a point on a map
 */
public class Point {
    private Location location;

    /**
     * Constructor for point
     *
     * @param lat current lat of point (double)
     * @param lon current lon of point (double)
     */
    Point(Double lat, Double lon){
        location = new Location(lat,lon);
    }


    // Setters

    /**
     * update the lat value of the point
     *
     * @param newLat double lat value
     */
    public void setLat(Double newLat){
        location.setLat(newLat);
    }

    /**
     * update the lon value of the point
     *
     *
     * @param newLon double lon value
     */
    public void setLon(Double newLon){
        location.setLon(newLon);
    }

    //Getters

    /**
     * get the current lat value of the point
     *
     * @return double - curr lat
     */
    public Double getLat(){
        return location.getLat();
    }

    /**
     * get the current lon value of the point
     *
     * @return double - curr lon
     */
    public Double getLon(){
        return location.getLon();
    }

}
