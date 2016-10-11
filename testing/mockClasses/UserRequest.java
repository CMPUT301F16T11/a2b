package com.example.brianofrim.a2btests;

import java.util.Calendar;

/**
 * Created by brianofrim on 2016-10-10.
 */
public class UserRequest {
    String startLocation;
    String endLocation;
    Number fare;
    long timeCreatedInMillis;
    Number requestId;

    UserRequest(String start, String end, Number intitialFare){
        startLocation = start;
        endLocation = end;
        fare = intitialFare;
        timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public Number getFare() {
        return fare;
    }

    public void setFare(Number fare) {
        this.fare = fare;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public float getFareEstimation(String startLocation, String endLocation) {
        return fare;
    }



}
