package com.cmput301f16t11.a2b;

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
    boolean accepted;

    UserRequest(String start, String end, Number intitialFare){
        startLocation = start;
        endLocation = end;
        fare = intitialFare;
        timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        accepted = false;
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

    public Number getFareEstimation(String startLocation, String endLocation) {
        return fare;
    }

    public void setAccepted(Boolean bool){
        accepted = bool;
    }

    public boolean getAccepted(){
        return accepted;
    }

    public boolean sentNotification() {
        return true;
    }
}
