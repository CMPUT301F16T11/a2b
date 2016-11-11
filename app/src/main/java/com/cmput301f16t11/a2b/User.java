package com.cmput301f16t11.a2b;

import java.util.ArrayList;
import java.util.Collection;

import io.searchbox.annotations.JestId;

/**
 * Model for User class
 */
public class User {
    @JestId
    private  String id;

    private transient ArrayList<UserRequest> requestsAsRider;
    private transient ArrayList<UserRequest> requestsAsDriver;
    private transient ArrayList<UserRequest> activeRequestsAsRider;
    private transient ArrayList<UserRequest> activeRequestsAsDriver;

    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
//    private Mode mode;

    User(){
//        mode = Mode.RIDER;
        requestsAsRider = new ArrayList<UserRequest>();
        requestsAsDriver = new ArrayList<UserRequest>();
        activeRequestsAsRider = new ArrayList<UserRequest>();
        activeRequestsAsDriver = new ArrayList<UserRequest>();
    }

    User(String name, String email) {
//        mode = Mode.RIDER;
        requestsAsRider = new ArrayList<UserRequest>();
        requestsAsDriver = new ArrayList<UserRequest>();
        activeRequestsAsRider = new ArrayList<UserRequest>();
        activeRequestsAsDriver = new ArrayList<UserRequest>();
        userName = name;
        this.email = email;
    }

    User(String name,  String email, String phone) {
//        mode = Mode.RIDER;
        requestsAsRider = new ArrayList<UserRequest>();
        requestsAsDriver = new ArrayList<UserRequest>();
        activeRequestsAsRider = new ArrayList<UserRequest>();
        activeRequestsAsDriver = new ArrayList<UserRequest>();
        userName = name;
        this.email = email;
        phoneNumber = phone;
    }


    User(String name, String pass, String email, String phone) {
//        mode = Mode.RIDER;
        requestsAsRider = new ArrayList<UserRequest>();
        requestsAsDriver = new ArrayList<UserRequest>();
        activeRequestsAsRider = new ArrayList<UserRequest>();
        activeRequestsAsDriver = new ArrayList<UserRequest>();
        userName = name;
        passWord = pass;
        this.email = email;
        phoneNumber = phone;
        this.id = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        try {
            User usr1 = (User) obj;
            if (usr1.getName().equals(this.getName())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


   //Getters
    public String getName(){
        return userName;
    }
    public String getPassWord() { return passWord;}
    public String getEmail() { return email;}
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public ArrayList<UserRequest> getRequestsAsRider() {
        /**
         * Returns all requests created by the user as a rider
         */
        return this.requestsAsRider;
    }
    public ArrayList<UserRequest> getActiveRequestsAsRider() {
        /**
         * Returns all ACTIVE requests created by the user as a rider
         */
        return this.activeRequestsAsRider;

    }
    public ArrayList<UserRequest> getRequestsAsDriver() {
        /**
         * Returns all requests that the user has accepted as a driver
         */
        return this.requestsAsDriver;
    }
    public ArrayList<UserRequest> getActiveRequestsAsDriver() {
        /**
         * Returns all ACTIVE requests that the user has accepted as a driver
         */
        return this.activeRequestsAsDriver;
    }


//    public UserRequest getLatestActiveDriverRequest() {
//        /**
//         * Automatically considers current mode of user (driver or rider) and returns the latests
//         * active request MADE AS A RIDER if mode == RIDER, and the latest active ACCEPTED REQUEST
//         * AS A DRIVER if mode == DRIVER.
//         */
//        return this.activeRequestsAsDriver.get(activeRequestsAsDriver.size() - 1);
//
//    }
//
//    public UserRequest getLatestActiveRiderRequest() {
//        /**
//         * Returns latest active request
//         */
//        return this.activeRequestsAsRider.get(activeRequestsAsRider.size() - 1);
//    }

//    public Mode getMode() {
//        return this.mode;
//    }
    public String getId() {return id;}

    @Override
    public String toString() {return this.userName;}

    //Setters
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setName(String name) {
        this.userName = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassWord(String pass) {
        this.passWord = pass;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setClosedRequestsAsDriver(Collection<UserRequest> requests) {
        this.requestsAsDriver.addAll(requests);
    }
    public void setClosedRequestsAsRider(Collection<UserRequest> requests) {
        this.requestsAsRider.addAll(requests);
    }
    public void setActiveRequestsAsRider(Collection<UserRequest> requests) {
        /**
         * add entire collection of active requests to rider list
         * also automatically adds to overall list of requests
         */
        this.activeRequestsAsRider.addAll(requests);
        setClosedRequestsAsRider(this.activeRequestsAsRider);
    }
    public void setActiveRequestsAsDriver(Collection<UserRequest> requests) {
        /**
         * add entire collection of active requests to driver list
         * also automatically adds to overall list of requests
         */
        this.activeRequestsAsDriver.addAll(requests);
        setClosedRequestsAsDriver(this.activeRequestsAsDriver);
    }

    // Request transactions
    public void addActiveRiderRequest(UserRequest request) {
        /**
         * Add an active request to the list of active rider requests
         * Automatically adds to overall list
         */
        this.activeRequestsAsDriver.add(request);
        this.addRiderRequest(request);
    }
    public void addActiveDriverRequest(UserRequest request) {
        /**
         * Add an active request to the list of active driver requests
         * Automatically adds to overall list
         */
        this.activeRequestsAsDriver.add(request);
        this.addDriverRequest(request);
    }
    public void addRiderRequest(UserRequest request) {
        /**
         * add a request to list of rider requests
         */
        this.requestsAsRider.add(request);
    }
    public void addDriverRequest(UserRequest request) {
        /**
         * Add a q request to list of driver requests
         */
        this.requestsAsDriver.add(request);
    }
    public void removeActiveRiderRequest(UserRequest request) {
        /**
         * Remove an active request from the list of active rider requests
         */
        this.activeRequestsAsRider.remove(request);
    }

    public int numberOfActiveRiderRequests() {

        return this.activeRequestsAsRider.size();
    }

    public int numberOfActiveDriverRequests() {
        return this.activeRequestsAsDriver.size();
    }

    public void notifyUser(UserRequest r) {
    }
//    public void setMode(Mode mode) {
//        this.mode = mode;
//    }

    public boolean hasAcceptedRequests(UserRequest request) {
        return true;
    }


}
