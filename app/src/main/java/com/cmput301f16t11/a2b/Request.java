package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.ArrayList;
import java.util.Date;

/**
 * Models requests
 */

public class Request {
    private LatLng startLocation;
    private LatLng endLocation;
    private Boolean completed;
    private User createdBy;
    private Date createdOn;
    private ArrayList<User> driversWhoAcceptedRequest;

    public Request(LatLng start, LatLng end, User creator) {
        this.startLocation = start;
        this.endLocation = end;
        this.createdBy = creator;
        this.createdOn = new Date();
        this.driversWhoAcceptedRequest = new ArrayList<User>();
    }

    // setters
    public void changeStartLocation(LatLng start) {
        this.startLocation = start;
    }

    public void changeEndLocation(LatLng end) {
        this.endLocation = end;
    }

    public void completeRequest() {
        this.completed = true;
    }

    public void addAcceptedDriver(User driver) {
        this.driversWhoAcceptedRequest.add(driver);
    }


    // getters
    public LatLng getStartLocation() {
        return this.startLocation;
    }

    public LatLng getEndLocation() {
        return this.endLocation;
    }

    public Boolean isComplete() {
        return this.completed;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public Date getCreatedon() {
        return this.createdOn;
    }

    public Boolean hasAcceptedDrivers() {
        if (this.numberOfAcceptedDrivers() > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Integer numberOfAcceptedDrivers() {
        return this.driversWhoAcceptedRequest.size();
    }

    public String toString() {
        String val = "Created by " + this.getCreatedBy() + "\n" + "Lat: " + "start: " +
                    this.getStartLocation() + "\n" + "end: " + this.getEndLocation() +
                    "\n" + this.getCreatedon();
        return val;
    }

}
