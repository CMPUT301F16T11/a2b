package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by brianofrim on 2016-10-13.
 */
public class RequestController {

    public static ArrayList<Request> getRequestNear(String address, Number radius){
        return new ArrayList<Request>();
    }

    public static ArrayList<Request> getRequestNear(LatLng location, Number radius) {
        return new ArrayList<Request>();
    }

    // TEMPORARY POPULATION OF RANDOM REQUESTS!!!
    public static ArrayList<Request> tempFakeRequestList() {
        ArrayList<Request> returnValue = new ArrayList<Request>();
        returnValue.add(new Request(new LatLng(53.5443890, -113.4909270),
                new LatLng(54.07777, -113.50192), new User("test", "test@email.com")));
        returnValue.add(new Request(new LatLng(54.07777, -113.50192),
                new LatLng(53.5443890, -113.4909270), new User("test", "test@email.com")));
        returnValue.add(new Request(new LatLng(54.07777, -113.50192),
                new LatLng(53.5443890, -113.4909270), new User("test2", "test2@email.com")));
        return returnValue;
    }

    public static ArrayList<Request> getNearbyRequests(LatLng location) {
        /**
         * For use in ride or drive mode
         * Gets all requests nearby to current location
         */
        //TODO: actual logic
        ArrayList<Request> temp = new ArrayList<Request>();
        temp.add(RequestController.tempFakeRequestList().get(0));
        return temp;
    }

    public static ArrayList<Request> getOwnRequests(User user) {
        /**
         * For use in ride mode only
         * Gets all requests created by the user
         */
        // TODO: actual logic
        // (gets requests created by this user)
        ArrayList<Request> temp = new ArrayList<Request>();
        temp.add(RequestController.tempFakeRequestList().get(1));
        return temp;
    }

    public static ArrayList<Request> getAcceptedByDrivers(User user) {
        /**
         * For use in ride mode only. Gets the requests that user has created and
         * are currently accepted by at least one driver.
         * Excludes completed requests.
         */
        // riders only
        //TODO: actual logic
        // (get requests accepted by a driver created by this user
        return new ArrayList<Request>();
    }

    public static ArrayList<Request> getAcceptedByUser(User user) {
        /**
         * For use in driver mode only. Gets the requests that user has currently
         * accepted, excluding completed requests.
         */
        // drivers only
        // TODO: actual logic
        // (get requests accepted by the curr user)
        return new ArrayList<Request>();
    }

    public static ArrayList<Request> getCompletedRequests(User user, Mode mode) {
        // TODO: actual logic
        /**
         * Gets the completed requests BY a driver if mode == Mode.DRIVER
         * Gets the completed requests a rider received if mode == Mode.RIDER
         */
    return new ArrayList<Request>();
    }

    public static ArrayList<Request> getConfirmedByRiders(User user, Mode mode) {
        //TODO: actual logic
        /**
        * get request accepted by the current user as a driver, that are also accepted by the user
        * who originally made the request
        * OR if the curr user is a rider, check the requests they have confirmed
         * Excludes completed requests
        */
        ArrayList<Request> temp = new ArrayList<Request>();
        temp.add(RequestController.tempFakeRequestList().get(2));
        return temp;
    }

}
