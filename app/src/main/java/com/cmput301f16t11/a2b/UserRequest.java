package com.cmput301f16t11.a2b;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.searchbox.annotations.JestId;

/**
 * Model representing UserRequests.
 * Contains distance, start, end locations, rider (who created the request), confirmed driver
 * (if any), fare and other details.
 */
public class UserRequest implements Parcelable {
    private ArrayList<String> acceptedDriverIds; // all the rider ids
    private String confirmedDriverId;
    private String riderId;
    private Point startLocation;
    private Point endLocation;
    private String startLocationName;
    private String endLocationString;

    private Number fare;
    private Double distance;
    private long timeCreatedInMillis;
    private Number requestId;
    private boolean accepted;
    private boolean completed;
    private boolean paymentReceived;
    private String description;
    private transient boolean inProgress;

    @JestId
    private String id;

    public UserRequest(LatLng start, LatLng end, Number fare, String riderId){
        this.startLocation = new Point(start.latitude,start.longitude);
        this.endLocation = new Point(end.latitude,end.longitude);
        this.fare = fare;
        this.riderId = riderId;
        this.timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        this.accepted = false;
        this.completed = false;
        this.paymentReceived = false;
        this.id = null;
        this.acceptedDriverIds = new ArrayList<>();

        //This is added so that we can search by a location name
        try {
            Geocoder geoCoder = new Geocoder(null);
            List<Address> startString = geoCoder.getFromLocation(start.latitude, end.longitude, 1);
            List<Address> endString = geoCoder.getFromLocation(end.latitude, end.longitude, 1);
            if(!startString.isEmpty()){
                this.startLocationName = startString.get(0).getAddressLine(0);
            }
            if(!endString.isEmpty()) {
                this.startLocationName = endString.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            Log.i("Error", "Unable to decode address");
            e.printStackTrace();
        }

    }

    public UserRequest(LatLng start, LatLng end, Number fare, String riderId, Double distance) {
        this.startLocation = new Point(start.latitude,start.longitude);
        this.endLocation = new Point(end.latitude,end.longitude);
        this.fare = fare;
        this.riderId = riderId;
        this.distance = distance;
        this.timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        this.accepted = false;
        this.completed = false;
        this.paymentReceived = false;
        this.id = null;
        acceptedDriverIds = new ArrayList<>();
    }

    public UserRequest(LatLng start, LatLng end, Number fare, String riderId, Double distance, String description, Context context) {
        this.startLocation = new Point(start.latitude,start.longitude);
        this.endLocation = new Point(end.latitude,end.longitude);
        this.fare = fare;
        this.riderId = riderId;
        this.distance = distance;
        this.timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        this.accepted = false;
        this.completed = false;
        this.paymentReceived = false;
        this.id = null;
        this.description = description;
        acceptedDriverIds = new ArrayList<>();

        try {
            //May investigate a better way of doing this context thing
            Geocoder geoCoder = new Geocoder(context);
            List<Address> startString = geoCoder.getFromLocation(start.latitude, end.longitude, 1);
            List<Address> endString = geoCoder.getFromLocation(end.latitude, end.longitude, 1);
            if(!startString.isEmpty()){
                this.startLocationName = startString.get(0).getAddressLine(0);
            }
            if(!endString.isEmpty()) {
                this.endLocationString= endString.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            Log.i("Error", "Unable to decode address");
            e.printStackTrace();
        }
    }

    public void clearAcceptedDrivers(){
        acceptedDriverIds.clear();
    }

    // Getters
    public String getConfirmedDriverID() {
        return this.confirmedDriverId;
    }

    public ArrayList<String> getAcceptedDriverIDs() {
        return this.acceptedDriverIds;
    }
    public String getRiderID() {
        return riderId;
    }
    public LatLng getEndLocation() {
        return new LatLng(this.endLocation.getLat(),this.endLocation.getLon());
    }
    public Number getFare() {
        return fare;
    }
    public LatLng getStartLocation() {
        return new LatLng(this.startLocation.getLat(), this.startLocation.getLon());
    }

    public boolean getAcceptedStatus(){
        return accepted;
    }
    public boolean isCompleted() {
        return completed;
    }
    public boolean sentNotification() {
        return true;
    } //TODO: actually check to see if notification was  sent
    public long getTimeCreatedInMillis() {
        return this.timeCreatedInMillis;
    }

    public String getDateString() {
        return new Date(this.timeCreatedInMillis).toString();
    }

    public Double getDistance() {
        return this.distance;
    }

    public String getId() {
        return id;
    }
    public String getDescription() {return description;}


    // setters
    public void setId(String id) {
        this.id = id;
    }

    public void setConfirmedDriver(String id) {
        this.confirmedDriverId = id;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation.setLat(startLocation.latitude);
        this.startLocation.setLon(startLocation.longitude);

    }

    public void setInProgress() {
        this.inProgress = true;
    }
    public void setEndLocation(LatLng endLocation) {
        this.endLocation.setLat(endLocation.latitude);
        this.endLocation.setLon(endLocation.longitude);
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
        return this.confirmedDriverId != null;
    }
    public void  addAcceptedDriver(String id){this.acceptedDriverIds.add(id);}
    public RequestStatus getRequestStatus() {
        if (this.confirmedDriverId == null) {
            if (this.acceptedDriverIds.size() == 0) {
                return RequestStatus.WAITING;
            }
            else {
                return RequestStatus.ACCEPTED;
            }
        }
        else {
            if (this.paymentReceived) {
                return RequestStatus.PAID;
            }
            if (this.completed) {
                return RequestStatus.COMPLETED;
            }
            return RequestStatus.CONFIRMED;
        }
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
        //out.writeParcelable(startLocation, flags);
        //out.writeParcelable(endLocation, flags);
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



    public String toString() {
        String temp = "Rider: " + this.getRiderID() + "\n";
        if (this.getConfirmedDriverID() != null) {
            temp = temp + "Confirmed Driver: " + this.getConfirmedDriverID() + "\n";
        }
        if (this.getDistance() != null) {
            temp = temp + "Distance: " + this.getDistance().toString() + "\n";
        }
        temp = temp + "Fare: " + this.getFare() + "\n" + "Created on: " +
                this.getDateString() + "\n";
        return temp;
    }
}
