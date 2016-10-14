package com.cmput301f16t11.a2b;

import org.junit.Test;
import static org.junit.Assert.*;

public class Story1UnitTest {
    /**
        Use Case: 1
        Id: US 01.01.01
        Description: A casual rider's car will not start and is in a rush to arrive.
        He uses a2b by selecting the start and end locations for the trip, and then
        requesting a ride between those two locations.

        Primary Actor: The casual rider
        Supporting Actor(s): Any drivers considering the request
        Goal: To submit a request for a ride between the two locations to the a2b system.
        Pre-conditions:

        App has access to the internet.
        Post-conditions:

        A request between the two locations is submitted to a2b servers.
        Basic Flow:

        1 The (logged in) rider selects a starting location
        2 The rider selects an end location
        3 The rider submits the request (with a fare offer?)
        Exceptions:

        1 - If there is no internet connection, system displays an error, loops to step 1 (eventually will use use case 21 instead - v2.0?)
     */

    User user;
    String userName = "billy";
    String passWord = "billy1zth3b35t";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10.00;

    public void setUp(){
      user = new User();
    }

    @Test
    public void user_auth_and_load(){

        boolean authenticated = UserController.auth(userName,passWord);
        if(authenticated){
            user = UserController.loadUser(userName);
            assertEquals("billy",user.getName());
        }else{
            fail("User not Authenticated");
        }

    }

    @Test
    public void create_user_request(){

        setUp();
        user.createRequest(startLocation,endLocation,fare);
        assertEquals(1,user.numberOfActiveRequests());
    }

    @Test
    public void check_request_start(){

        setUp();
        user.createRequest(startLocation,endLocation,fare);
        UserRequest request = user.getLatestActiveRequest();
        assertEquals(startLocation, request.getStartLocation());
    }

    @Test
    public void check_request_end(){

        setUp();
        user.createRequest(startLocation,endLocation,fare);
        UserRequest request = user.getLatestActiveRequest();
        assertEquals(endLocation, request.getEndLocation());
    }

    @Test
    public void check_request_fare(){

        setUp();
        user.createRequest(startLocation,endLocation,fare);
        UserRequest request = user.getLatestActiveRequest();
        assertEquals(fare, request.getFare());
    }

}