package com.cmput301f16t11.a2b;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 Use Case: 13
 ID: US 04.01.01
 Description: A driver browses through nearby (or to any geo-location) requests
 Primary Actor: The driver Supporting Actor(s): Riders placing requests Goal: To view nearby requests (what should this radius be?)
 Trigger: The driver requests to view requests near a specific geolocation
 Pre-conditions:

 Connection to the internet
 Post-conditions:

 Should not have any effect on requests, status, etc
 Basic Flow:

 1 The driver views the main page
 2 The driver selects a geolocation, either their current location or manually
 3 The driver touches the search for requests nearby button
 Exceptions:

 2 Cannot find location -> reset to 1
 */
public class Story13UnitTest{
    User rider1 =  UserController.loadUser("rider1"); // rider
    User rider2 =  UserController.loadUser("rider2"); // rider
    User rider3 =  UserController.loadUser("rider3"); // rider
    User rider4 =  UserController.loadUser("rider4"); // rider

    User driver = UserController.loadUser("billy"); // driver

    String startLocation1 = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation1 = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    String startLocation2 = "8210 106 St NW Edmonton, AB T6E 5T2";
    String endLocation2 = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    String startLocation3 = "8210 109 St NW Edmonton, AB T6E 5T2";
    String endLocation3 = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    String startLocation4 = "8210 110 St NW Edmonton, AB T6E 5T2";
    String endLocation4 = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";

    UserRequest req1;
    UserRequest req2;
    UserRequest req3;
    UserRequest req4;


    private void setUp() {
        rider1.createRequest(startLocation1,endLocation1,10.00);
        rider2.createRequest(startLocation2,endLocation2,10.00);
        rider3.createRequest(startLocation3,endLocation3,10.00);
        rider4.createRequest(startLocation4,endLocation4,10.00);
        req1 = rider1.getLatestActiveRequest();
        req2 = rider2.getLatestActiveRequest();
        req3 = rider3.getLatestActiveRequest();
        req4 = rider4.getLatestActiveRequest();
    }

    @Test
    public void checkListOfNearbyRequests(){
        setUp();
        ArrayList<UserRequest> nearbyRequests = RequestController.getRequestNear(startLocation1,20);
        assertEquals(4, nearbyRequests.size());
    }

}
