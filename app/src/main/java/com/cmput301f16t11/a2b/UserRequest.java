package com.cmput301f16t11.a2b;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private String riderUsername;
    private Point startLocation;
    private Point endLocation;
    private String startLocationName;
    private String endLocationName;

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

    /**
     * Instantiates a new User request.
     *
     * @param start             the start
     * @param end               the end
     * @param fare              the fare
     * @param riderId           the rider id
     * @param riderUsername     the rider username
     * @param distance          the distance
     * @param description       the description
     * @param startLocationName the start location name
     * @param endLocationString the end location string
     */
    public UserRequest(LatLng start, LatLng end, Number fare, String riderId, String riderUsername, Double distance, String description,
                       String startLocationName, String endLocationString) {
        this.startLocation = new Point(start.latitude, start.longitude);
        this.endLocation = new Point(end.latitude, end.longitude);
        this.fare = fare;
        this.riderUsername = riderUsername;
        this.riderId = riderId;
        this.timeCreatedInMillis = Calendar.getInstance().getTimeInMillis();
        this.accepted = false;
        this.completed = false;
        this.paymentReceived = false;
        this.description = description;
        this.id = null;
        this.distance = distance;
        this.acceptedDriverIds = new ArrayList<>();
        this.startLocationName = startLocationName;
        this.endLocationName = endLocationString;
    }

    /**
     * Instantiates a new User request.
     *
     * @param start   the start
     * @param end     the end
     * @param fare    the fare
     * @param riderId the rider id
     */
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

    }

    /**
     * Clear accepted drivers.
     */
    public void clearAcceptedDrivers(){
        acceptedDriverIds.clear();
    }

    /**
     * Gets confirmed driver id.
     *
     * @return the confirmed driver id
     */
// Getters
    public String getConfirmedDriverID() {
        return this.confirmedDriverId;
    }

    /**
     * Gets accepted driver i ds.
     *
     * @return the accepted driver i ds
     */
    public ArrayList<String> getAcceptedDriverIDs() {
        return this.acceptedDriverIds;
    }

    /**
     * Gets rider id.
     *
     * @return the rider id
     */
    public String getRiderID() {
        return riderId;
    }

    /**
     * Gets end location.
     *
     * @return the end location
     */
    public LatLng getEndLocation() {
        return new LatLng(this.endLocation.getLat(),this.endLocation.getLon());
    }

    /**
     * Gets fare.
     *
     * @return the fare
     */
    public Number getFare() {
        return fare;
    }

    /**
     * Gets start location.
     *
     * @return the start location
     */
    public LatLng getStartLocation() {
        return new LatLng(this.startLocation.getLat(), this.startLocation.getLon());
    }

    /**
     * Get accepted status boolean.
     *
     * @return the boolean
     */
    public boolean getAcceptedStatus(){

        return accepted;
    }

    /**
     * Is completed boolean.
     *
     * @return the boolean
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Sent notification boolean.
     *
     * @return the boolean
     */
    public boolean sentNotification() {
        return true;
    }

    /**
     * Get start location name string.
     *
     * @return the string
     */
    public String getStartLocationName(){
        return startLocationName;
    }

    /**
     * Get end location name string.
     *
     * @return the string
     */
    public String getEndLocationName(){
        return endLocationName;
    }

    /**
     * Gets date string.
     *
     * @return the date string
     */
    public String getDateString() {
        return new Date(this.timeCreatedInMillis).toString();
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public Double getDistance() {
        return this.distance;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get rider username string.
     *
     * @return the string
     */
    public String getRiderUsername(){return riderUsername;}

    /**
     * Sets id.
     *
     * @param id the id
     */
// setters
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Sets confirmed driver.
     *
     * @param id the id
     */
    public void setConfirmedDriver(String id) {

        this.confirmedDriverId = id;
    }

    /**
     * Sets start location.
     *
     * @param startLocation the start location
     */
    public void setStartLocation(LatLng startLocation) {

        this.startLocation.setLat(startLocation.latitude);
        this.startLocation.setLon(startLocation.longitude);
    }

    /**
     * Sets in progress.
     */
    public void setInProgress() {

        this.inProgress = true;
    }

    /**
     * Sets end location.
     *
     * @param endLocation the end location
     */
    public void setEndLocation(LatLng endLocation) {
        this.endLocation.setLat(endLocation.latitude);
        this.endLocation.setLon(endLocation.longitude);
    }

    /**
     * Sets payment received.
     *
     * @param paymentReceived the payment received
     */
    public void setPaymentReceived(boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    /**
     * Sets fare.
     *
     * @param fare the fare
     */
    public void setFare(Number fare) {
        this.fare = fare;
    }

    /**
     * Set accepted status.
     *
     * @param bool the bool
     */
    public void setAcceptedStatus(Boolean bool){

        accepted = bool;
    }

    /**
     * Sets completed status.
     *
     * @param completed the completed
     */
    public void setCompletedStatus(boolean completed) {

        this.completed = completed;
    }

    /**
     * Set start location name.
     *
     * @param startLocationName the start location name
     */
    public void setStartLocationName(String startLocationName){
        this.startLocationName = startLocationName;
    }

    /**
     * Set end location name.
     *
     * @param endLocationName the end location name
     */
    public void setEndLocationName(String endLocationName){
        this.endLocationName = endLocationName;
    }

    /**
     * Set distance.
     *
     * @param distance the distance
     */
    public void setDistance(Double distance){
        this.distance = distance;
    }

    /**
     * Is payment recived boolean.
     *
     * @return the boolean
     */
    public boolean isPaymentRecived() {

        return paymentReceived;
    }

    /**
     * Has confirmed rider boolean.
     *
     * @return the boolean
     */
    public boolean hasConfirmedRider() {

        return this.confirmedDriverId != null;
    }

    /**
     * Add accepted driver.
     *
     * @param id the id
     */
    public void  addAcceptedDriver(String id){
        this.acceptedDriverIds.add(id);
    }

    /**
     * Gets request status.
     *
     * @return the request status
     */
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        UserRequest request;
        try {
            request = (UserRequest) obj;
        } catch (Exception e) {
            return false;
        }
        if (request.getFare().equals(this.getFare())
                && request.getStartLocation().equals(this.getStartLocation()) &&
                request.getEndLocation().equals(this.getEndLocation()) &&
                request.getDateString().equals(this.getDateString())) {
            return true;
        }
        return false;
    }
}
