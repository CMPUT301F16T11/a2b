package com.cmput301f16t11.a2b;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by tothd on 10/24/2016.
 */

public class RequestsUnitTest {
    private User user;
    private UserRequest request;
    private String userName = "user";
    private String passWord = "pass";
    private String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    private String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    private Number fare = 10.00;
    private String newDriverPhoneNumber = "780-666-0000";
    private String newDriverEmail = "user@domain.com";

    public void setUp(){
        user = new User();
        user.createRequest(startLocation,endLocation,fare);
        request = user.getLatestActiveRequest();
    }



    /**
     US 01.01.01
     As a rider, I want to request rides between two locations.*/
    @Test
    public void testUserAuthenticated(){

        boolean authenticated = UserController.auth(userName,passWord);
        if(authenticated){
            user = UserController.loadUser(userName);
            assertEquals("user",user.getName());
        }else{
            fail("User not Authenticated");
        }

    }

    @Test
    public void createUserRequest(){

        setUp();

        request = user.getRequests().get(0);
        assertEquals(1,user.numberOfActiveRequests());
    }

    @Test
    public void checkRequestStart(){

        setUp();
        assertEquals(startLocation, request.getStartLocation());
    }

    @Test
    public void checkRequestEnd(){

        setUp();
        assertEquals(endLocation, request.getEndLocation());
    }

    @Test
    public void checkRequestFare(){

        setUp();
        assertEquals(fare, request.getFare());
    }

    /** Based on US 01.02.01
    To Test: Logging in Check Open requests
    */

    @Test
    public void testFare(){

        setUp();
        assertEquals(request.getFare(), fare);
    }


    /**
     US 01.03.01
     As a rider, I want to be notified if my request is accepted.
     */
    @Test
    public void testRiderNotification() {

        setUp();
        user.addAcceptedRequest(request);
        user.notifyUser(request);
        assertTrue(request.sentNotification());

    }
    /**
     * US 01.04.01
     *  As a rider, I want to cancel requests.
    */
    @Test
    public void testDeleteRequest() {
        user = UserController.loadUser(userName);
        user.createRequest(startLocation, endLocation, fare);
        Assert.assertEquals(1, user.numberOfActiveRequests());
        user.removeRequest(user.getLatestActiveRequest());
        Assert.assertEquals(0, user.numberOfActiveRequests());
    }
    /**
     * US 01.05.01
     * As a rider, I want to be able to phone or email the driver who accepted a request.
     */
    @Test
    public void testGetDriverNumber(){
        // To do: get driver from request
        setUp();
        user.setEmail(newDriverEmail);
        assertEquals(newDriverPhoneNumber, user.getPhoneNumber());
    }

    @Test
    public void testGetDriverEmail(){
        // To do: get driver from request
        setUp();
        user.setPhoneNumber(newDriverPhoneNumber);
        assertEquals(newDriverEmail, user.getEmail());
    }
    /**
     * US 01.06.01
     As a rider, I want an estimate of a fair fare to offer to drivers.
     */
    @Test
    public void testFareEstimation() {
        setUp();

        UserRequest request = user.getLatestActiveRequest();
        fare = request.getFareEstimation(startLocation, endLocation);
        assertEquals(15, fare); // will hard code the the correct estimation when
    }

    /**
     * US 01.07.01
     As a rider, I want to confirm the completion of a request and enable payment.
     */
    @Test
    public void checkPayment() {
        setUp();
        request.setPaymentReceived(true); // rider pays
        assertEquals(true,request.isPaymentRecived());
    }
    /**
     * US 01.08.01
     As a rider, I want to confirm a driver's acceptance.
     This allows us to choose from a list of acceptances if more
     than 1 driver accepts simultaneously.
     */

    @Test
    public void testUserAcceptancePending() {

        user = UserController.loadUser(userName);
        user.createRequest(startLocation, endLocation, fare);
        User user = new User();
        UserRequest request = new UserRequest("bottom", "here", 10);
        user.addAcceptedRequest(request);
        assertTrue(user.hasAcceptedRequests(request));
    }


}
