package com.cmput301f16t11.a2b;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Created by brianofrim on 2016-10-10.
 *
 * Edited to implement parcelable to allow userrequests to be passed to dialogs
 */
public class UserRequest implements Parcelable {
    private String driver;
    private String rider;
    LatLng startLocation;
    LatLng endLocation;
    Number fare;
    long timeCreatedInMillis;
    Number requestId;
    boolean accepted;
    boolean completed;
    boolean paymentReceived;

    UserRequest(LatLng start, LatLng end, Number intitialFare){
        startLocation = start;
        endLocation = end;
        fare = intitialFare;
        timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        accepted = false;
        completed = false;
        paymentReceived = false;
    }

    public String getDriver() {return driver;}
    public String getRider() {return rider;}
    public LatLng getEndLocation() {
        return endLocation;
    }
    public Number getFare() {
        return fare;
    }
    public LatLng getStartLocation() {
        return startLocation;
    }
    public Number getFareEstimation(String startLocation, String endLocation) {
        return fare;
    }
    public boolean getAcceptedStatus(){
        return accepted;
    }

    public void setDriver(String d) {driver = d;}
    public void setRider(String r) {rider = r;}
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

    public boolean isCompleted() {
        return completed;
    }
    public boolean isPaymentRecived() {
        return paymentReceived;
    }

    public boolean sentNotification() {
        return true;
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
        out.writeString(driver);
        out.writeString(rider);
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
        driver = in.readString();
        rider = in.readString();
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
}
