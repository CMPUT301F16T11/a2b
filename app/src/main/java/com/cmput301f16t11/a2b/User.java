package com.cmput301f16t11.a2b;

import java.util.ArrayList;
import java.util.Collection;

import io.searchbox.annotations.JestId;

/**
 * User class model, contains username, email and phone number
 */
public class User {
    @JestId
    private String id;

    private transient ArrayList<UserRequest> requestsAsRider;
    private transient ArrayList<UserRequest> requestsAsDriver;
    private transient ArrayList<UserRequest> activeRequestsAsRider;
    private static transient ArrayList<UserRequest> activeRequestsAsDriver;


    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
    private Vehicle car;
    private double rating;
    private int numRatings;
    private int totalRating;
    private Boolean canDrive;

//    private Mode mode;

    /**
     * null constructor for user
     */
    User() {
//        mode = Mode.RIDER;
        requestsAsRider = new ArrayList<UserRequest>();
        requestsAsDriver = new ArrayList<UserRequest>();
        activeRequestsAsRider = new ArrayList<UserRequest>();
        activeRequestsAsDriver = new ArrayList<UserRequest>();
    }

    /**
     * constructor for user with name and email
     *
     * @param name  username
     * @param email email
     */

    User(String name, String email, String phone) {
//        mode = Mode.RIDER;
        requestsAsRider = new ArrayList<UserRequest>();
        requestsAsDriver = new ArrayList<UserRequest>();
        activeRequestsAsRider = new ArrayList<UserRequest>();
        activeRequestsAsDriver = new ArrayList<UserRequest>();
        userName = name;
        this.email = email;
        this.canDrive = false;
        phoneNumber = phone;
        numRatings = 0;
        totalRating = 0;
        rating = -1;
    }

    User(String name, String email, String phone, Vehicle car) {
//        mode = Mode.RIDER;
        requestsAsRider = new ArrayList<UserRequest>();
        requestsAsDriver = new ArrayList<UserRequest>();
        activeRequestsAsRider = new ArrayList<UserRequest>();
        activeRequestsAsDriver = new ArrayList<UserRequest>();
        userName = name;
        this.email = email;
        this.canDrive = true;
        phoneNumber = phone;
        this.car = car;
        numRatings = 0;
        totalRating = 0;
        rating = -1;
    }


    /**
     * constructor to fill all fields
     *
     * @param name  username
     * @param pass  password?
     * @param email email address
     * @param phone phone number
     */
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
        if (obj == null) {
            return false;
        }
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
    public Vehicle getCar(){return car;}
    /**
     * gets user name  of user
     *
     * @return curr username
     */
    public String getName() {
        return userName;
    }

    /**
     * gets password? of user
     *
     * @return the curr password
     * @deprecated no passwords!
     */

    /**
     * Check whether this user has signed up to include driving capabilities
     *
     * @return true if you are signed up to drive
     */
    public Boolean canDrive() {
        return this.canDrive;
    }
    public String getPassWord() {
        return passWord;
    }

    /**
     * gets email of user
     *
     * @return gets curr email
     */
    public String getEmail() {
        return email;
    }

    /**
     * gets curr phone number of user
     *
     * @return the curr phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * gets a string version of the phone numeber - (xxx)123-4567
     *
     * @return the formatted string
     */
    public String getFormattedPhoneNumber() {
        String temp = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) +
                "-" + phoneNumber.substring(6, 10);
        return temp;
    }

    /**
     * returns all active requests created by the user as a rider
     *
     * @return list of UserRequests created by rider that are active
     */
    public ArrayList<UserRequest> getActiveRequestsAsRider() {
        /**
         * Returns all ACTIVE requests created by the user as a rider
         */
        return this.activeRequestsAsRider;

    }
    public ArrayList<UserRequest> getActiveRequestsAsDriver() {
        /**
         * Returns all ACTIVE requests created by the user as a rider
         */
        return this.requestsAsDriver;

    }

    /**
     * Method to get the user's current rating
     *
     * @return rating : int
     */
    public int getNumRatings() {return numRatings;}

    public int getTotalRating() {return totalRating;}

    public double getRating() {return rating;}


    /**
     * get user id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.userName;
    }

    //Setters

    /**
     * Sets phone number
     *
     * @param phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets username
     *
     * @param name new username
     */
    public void setName(String name) {
        this.userName = name;
    }

    /**
     * set email
     *
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * set password
     *
     * @param pass new password
     */

    /**
     * Allow a rider to drive or remove the ability of a rider/driver to drive
     *
     * @param ability true if ability to drive, false otherwise
     */
    public void setDriveAbility(Boolean ability) {
        this.canDrive = ability;
    }
    public void setPassWord(String pass) {
        this.passWord = pass;
    }

    /**
     * set user id
     *
     * @param id new id
     */
    public void setId(String id) {
        this.id = id;
    }

    public void setRating(double i) {rating = i;}

    public void setTotalRating(int i) {totalRating = i;}

    public void setNumRatings(int i) {numRatings = i;}

    /**
     * set a closed request by driver
     *
     * @param requests list of UserRequests
     */
    public void setClosedRequestsAsDriver(Collection<UserRequest> requests) {
        this.requestsAsDriver.addAll(requests);
    }

    /**
     * Set the car obj for the user
     *
     * @param vehicle the car object
     */
    public void setCar(Vehicle vehicle) {
        this.car = vehicle;
    }

    /**
     * set a closed request for rider
     *
     * @param requests list of UserRequests
     */
    public void setClosedRequestsAsRider(Collection<UserRequest> requests) {
        this.requestsAsRider.addAll(requests);
    }

    /**
     * sets active requests as rider
     *
     * @param requests list of UserRequests
     */
    public void setActiveRequestsAsRider(Collection<UserRequest> requests) {
        /**
         * add entire collection of active requests to rider list
         * also automatically adds to overall list of requests
         */
        this.activeRequestsAsRider.addAll(requests);
        setClosedRequestsAsRider(this.activeRequestsAsRider);
    }

    /**
     * set active requests as driver
     *
     * @param requests list of UserRequests
     */
    public void setActiveRequestsAsDriver(Collection<UserRequest> requests) {
        /**
         * add entire collection of active requests to driver list
         * also automatically adds to overall list of requests
         */
        this.activeRequestsAsDriver.addAll(requests);
        setClosedRequestsAsDriver(this.activeRequestsAsDriver);
    }

    // Request transactions

    /**
     * add an active rider request
     *
     * @param request UserRequest
     */
    public void addActiveRiderRequest(UserRequest request) {
        /**
         * Add an active request to the list of active rider requests
         * Automatically adds to overall list
         */
        this.activeRequestsAsRider.add(request);
        this.addRiderRequest(request);
    }

    /**
     * Add an active driver request
     *
     * @param request UserRequest
     */
    public void addActiveDriverRequest(UserRequest request) {
        /**
         * Add an active request to the list of active driver requests
         * Automatically adds to overall list
         */
        this.activeRequestsAsDriver.add(request);
        this.addDriverRequest(request);
    }

    /**
     * add a rider request
     *
     * @param request UserRequest
     */
    public void addRiderRequest(UserRequest request) {
        /**
         * add a request to list of rider requests
         */
        this.requestsAsRider.add(request);
    }

    /**
     * Add a driver request
     *
     * @param request UserRequest
     */
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

    public boolean hasAcceptedRequests(UserRequest request) {
        return request.getAcceptedStatus();
    }

    public void notifyUser(UserRequest r) {
        //TODO:connect this with Rider/DriverNotificationService
    }

}
