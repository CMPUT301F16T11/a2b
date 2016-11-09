package com.cmput301f16t11.a2b;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Model representing UserRequests.
 * Contains distance, start, end locations, rider (who created the request), confirmed driver
 * (if any), fare and other details.
 */
public class UserRequest implements Parcelable {
    private ArrayList<User> acceptedDrivers;
    private User confirmedDriver;
    private User rider;
    private LatLng startLocation;
    private LatLng endLocation;
    private Number fare;
    private Double distance;
    private long timeCreatedInMillis;
    private Number requestId;
    private boolean accepted;
    private boolean completed;
    private boolean paymentReceived;

    public UserRequest(LatLng start, LatLng end, Number fare, User rider){
        this.startLocation = start;
        this.endLocation = end;
        this.fare = fare;
        this.rider = rider;
        this.timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        this.accepted = false;
        this.completed = false;
        this.paymentReceived = false;
    }

    public UserRequest(LatLng start, LatLng end, Number fare, User rider, Double distance) {
        this.startLocation = start;
        this.endLocation = end;
        this.fare = fare;
        this.rider = rider;
        this.distance = distance;
        this.timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        this.accepted = false;
        this.completed = false;
        this.paymentReceived = false;
    }

    // Getters
    public User getConfirmedDriver() {
        return this.confirmedDriver;
    }

    public ArrayList<User> getAcceptedDrivers() {
        return this.acceptedDrivers;
    }
    public User getRider() {
        return rider;
    }
    public LatLng getEndLocation() {
        return endLocation;
    }
    public Number getFare() {
        return fare;
    }
    public LatLng getStartLocation() {
        return startLocation;
    }
    public boolean getAcceptedStatus(){
        return accepted;
    }
    public boolean isCompleted() {
        return completed;
    }
    public boolean sentNotification() {
        return true;
    }
    public long getTimeCreatedInMillis() {
        return this.timeCreatedInMillis;
    }

    public String getDateString() {
        return new Date(this.timeCreatedInMillis).toString();
    }

    public Double getDistance() {
        return this.distance;
    }

    // setters

    public void setConfirmedDriver(User d) {
        this.confirmedDriver = d;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }
    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }
    public void setPaymentReceived(boolean paymentRecived) {
        this.paymentReceived = paymentRecived;
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
    public boolean isPaymentRecived() {
        return paymentReceived;
    }
    public boolean hasConfirmedRider() {
        return this.confirmedDriver != null;
    }


    /* Parcelable Stuff */

    /**
     * Writes data to the inputted parcel
     *
     * Byte reading writing taken on Nov 7 2016 from
     * http://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
     *
     * @param out : Parcel
     * @param flags : int
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(startLocation, flags);
        out.writeParcelable(endLocation, flags);
        out.writeInt((int)fare);
        out.writeLong(timeCreatedInMillis);
        out.writeInt((int)requestId);
        out.writeByte((byte)(accepted?1:0));
        out.writeByte((byte)(completed?1:0));
        out.writeByte((byte)(paymentReceived?1:0));
    }

    /**
     * Creator to create UserRequest based on a parcel
     *
     * @param in : Parcel
     */
    public UserRequest(Parcel in) {
        startLocation = in.readParcelable(LatLng.class.getClassLoader());
        endLocation = in.readParcelable(LatLng.class.getClassLoader());
        fare = in.readInt();
        timeCreatedInMillis = in.readLong();
        requestId = in.readInt();
        accepted = in.readByte() != 0;
        completed = in.readByte() != 0;
        paymentReceived = in.readByte() != 0;
    }

    /**
     * Static variable to create user requests from a parcel
     *
     */
    public static final Parcelable.Creator<UserRequest> CREATOR =
            new Parcelable.Creator<UserRequest>() {
                public UserRequest createFromParcel(Parcel in) {
                    return new UserRequest(in);
                }

                public UserRequest[] newArray(int size) {
                    return new UserRequest[size];
                }
            };

    public int describeContents() {
        // Not sure what goes here
        return 0;
    }

    public String getId() {
        //TODO: ALL ID WORK
        return "NEEDS TO BE IMPLEMENTED";
    }

    public String toString() {
        String temp = "Rider: " + this.getRider() + "\n";
        if (this.getConfirmedDriver() != null) {
            temp = temp + "Confirmed Driver: " + this.getConfirmedDriver() + "\n";
        }
        if (this.getDistance() != null) {
            temp = temp + "Distance: " + this.getDistance().toString() + "\n";
        }
        temp = temp + "Fare: " + this.getFare() + "\n" + "Created on: " +
                this.getDateString() + "\n";
        return temp;
    }
}
