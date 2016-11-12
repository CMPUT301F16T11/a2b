package com.cmput301f16t11.a2b;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by tothd on 11/10/2016.
 */

public class MockUser extends User {

    private transient ArrayList<MockUserRequest> requestsAsRider;
    private transient ArrayList<MockUserRequest> requestsAsDriver;
    private transient ArrayList<MockUserRequest> activeRequestsAsRider;
    private transient ArrayList<MockUserRequest> activeRequestsAsDriver;

    public MockUser(){
        super();
        requestsAsRider = new ArrayList<>();
        requestsAsDriver = new ArrayList<>();
        activeRequestsAsRider = new ArrayList<>();
        activeRequestsAsDriver = new ArrayList<>();
    }

//    public static ArrayList<MockUserRequest> getActiveRequestsAsDriver() {
//        /**
//         * Returns all ACTIVE requests that the user has accepted as a driver
//         */
//        return this.activeRequestsAsDriver;
//    }
//    @Override
//    public ArrayList<MockUserRequest> getActiveRequestsAsRider() {
//        /**
//         * Returns all ACTIVE requests created by the user as a rider
//         */
//        return this.activeRequestsAsRider;
//
//    }
//    @Override
//    public void setClosedRequestsAsDriver(Collection<MockUserRequest> requests) {
//        this.requestsAsDriver.addAll(requests);
//    }
//    @Override
//    public void setClosedRequestsAsRider(Collection<MockUserRequest> requests) {
//        this.requestsAsRider.addAll(requests);
//    }
//    @Override
//    public void setActiveRequestsAsRider(Collection<MockUserRequest> requests) {
//        /**
//         * add entire collection of active requests to rider list
//         * also automatically adds to overall list of requests
//         */
//        this.activeRequestsAsRider.addAll(requests);
//        setClosedRequestsAsRider(this.activeRequestsAsRider);
//    }
//    public void setActiveRequestsAsDriver(Collection<UserRequest> requests) {
//        /**
//         * add entire collection of active requests to driver list
//         * also automatically adds to overall list of requests
//         */
//        this.activeRequestsAsDriver.addAll(requests);
//        setClosedRequestsAsDriver(this.activeRequestsAsDriver);
//    }
//
//    // Request transactions
//    public void addActiveRiderRequest(UserRequest request) {
//        /**
//         * Add an active request to the list of active rider requests
//         * Automatically adds to overall list
//         */
//        this.activeRequestsAsDriver.add(request);
//        this.addRiderRequest(request);
//    }
//    public void addActiveDriverRequest(UserRequest request) {
//        /**
//         * Add an active request to the list of active driver requests
//         * Automatically adds to overall list
//         */
//        this.activeRequestsAsDriver.add(request);
//        this.addDriverRequest(request);
//    }
//    public void addRiderRequest(UserRequest request) {
//        /**
//         * add a request to list of rider requests
//         */
//        this.requestsAsRider.add(request);
//    }
//    public void addDriverRequest(UserRequest request) {
//        /**
//         * Add a q request to list of driver requests
//         */
//        this.requestsAsDriver.add(request);
//    }
}
