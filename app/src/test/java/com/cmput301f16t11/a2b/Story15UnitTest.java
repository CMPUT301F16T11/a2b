package com.cmput301f16t11.a2b;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 Use Case: 15
 ID: US 05.01.01
 Description: A driver wants to accept an open request as well as payment upon completion
 Primary Actor: The driver
 Supporting Actor(s): the rider
 Goal: To accept an open request and payment
 Trigger: The driver accepts an open request, and then completes said request
 Pre-conditions:

 Connection to the internet
 An open request exists for the driver to accept
 Post-conditions:

 The request is completed
 Payment has been made
 The driver has completed the request
 Basic Flow:

 1 The driver views open requests
 2 The drives accepts a request
 3 The rider confirms the acceptance
 4 The driver drives the rider and marks the request as complete
 5 The rider processes payment and marks the request as complete
 Exceptions:

 3 Rider does not confirm acceptance -> Timeout??
 4 Driver does not drive the rider -> Give rider option to protest?
 5 rider does not correctly process payment -> notify the rider
 */
public class Story15UnitTest{
    User rider =  UserController.loadUser("some1"); // rider
    User driver =  UserController.loadUser("somedriver"); // rider
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    UserRequest req;


    private void setUp() throws Exception {
        rider.createRequest(startLocation,endLocation,10.00);
        req = rider.getLatestActiveRequest();

    }

    @Test
    public void driver_accept() throws Exception {
        setUp();
        driver.addAcceptedRequest(req);
        assertEquals(1,driver.getAcceptedRequests().size());
    }

    @Test
    public void rider_confirm_accept() throws Exception {
        setUp();
        req.setAcceptedStatus(true);
        assertTrue(req.getAcceptedStatus());
    }

    @Test
    public void driver_complete() throws Exception {
        setUp();
        req.setCompletedStatus(true);
        assertTrue(req.isCompleted());
    }

    @Test
    public void rider_payment_complete() throws Exception {
        setUp();
        req.setPaymentReceived(true);
        assertTrue(req.isPaymentRecived());
    }


}
