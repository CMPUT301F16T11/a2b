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
    boolean completed;
    boolean paymentRecived;

    UserRequest(String start, String end, Number intitialFare){
        startLocation = start;
        endLocation = end;
        fare = intitialFare;
        timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        accepted = false;
        completed = false;
        paymentRecived = false;
    }

    public String getEndLocation() {
        return endLocation;
    }
    public Number getFare() {
        return fare;
    }
    public String getStartLocation() {
        return startLocation;
    }
    public Number getFareEstimation(String startLocation, String endLocation) {
        return fare;
    }
    public boolean getAcceptedStatus(){
        return accepted;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
    public void setPaymentReceived(boolean paymentRecived) {
        this.paymentRecived = paymentRecived;
    }
    public void setFare(Number fare) {
        this.fare = fare;
    }
    public void setAcceptedStatus(Boolean bool){
        accepted = bool;
    }
    public void setCompletedStatus(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }
    public boolean isPaymentRecived() {
        return paymentRecived;
    }

    public boolean sentNotification() {
        return true;
    }
}
