package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by tothd on 10/24/2016.
 */

public class RequestsUnitTest {
    private User user;
    private UserRequest request;
    LatLng startLocation = new LatLng(100,100);
    LatLng endLocation = new LatLng(25,25);
    double fare = 10;

    @Before
    public void setUp(){
        user = new User();
        request = new UserRequest(startLocation,endLocation,fare,user.getId());
        user.addActiveRiderRequest(request);

    }


    /**
     US 01.01.01
     As a rider, I want to request rides between two locations.*/

    @Test
    public void checkRequestStart(){
        //Check that the start location is the same as given start location
        assertEquals(startLocation, request.getStartLocation());
    }

    @Test
    public void checkRequestEnd(){
        //Check continuity between end locations
        assertEquals(endLocation, request.getEndLocation());
    }

    @Test
    public void checkRequestFare(){
        assertEquals(fare, request.getFare());
    }

    /** Based on US 01.02.01
     As a rider, I want to see current requests I have open.
     */

    @Test
    public void checkOpenRequests(){
        assertEquals(request,user.getActiveRequestsAsRider().get(0));
    }


    /**
     US 01.03.01
     As a rider, I want to be notified if my request is accepted.
     */
    @Test
    public void testRiderNotification() {
        request.addAcceptedDriver("someId");
        assertTrue(request.getAcceptedDriverIDs().size() > 0);

    }
    /**
     * US 01.04.01
     *  As a rider, I want to cancel requests.
    */
    @Test
    public void testDeleteRequest() {
        Assert.assertEquals(1, user.getActiveRequestsAsRider().size());
        user.removeActiveRiderRequest(request);
        Assert.assertEquals(0, user.getActiveRequestsAsRider().size());
        }
    /**
     * US 01.05.01
     * As a rider, I want to be able to phone or email the driver who accepted a request.
     *
     */
    @Test
    public void testGetDriverNumber(){

        user.setEmail("user@mail.com");
        assertEquals("user@mail.com", user.getEmail());
    }

    @Test
    public void testGetDriverEmail(){
        // To do: get driver from request
        user.setPhoneNumber("7805551234");
        assertEquals("7805551234", user.getPhoneNumber());
    }
    /**
     * US 01.06.01
     As a rider, I want an estimate of a fair fare to offer to drivers.
     */
    @Test
    public void testFareEstimation() {
        // Expected 21.25 from a distance of 15 kms
        Number fairFare = RiderLocationActivity.FairEstimation.estimateFair("15.0");
        request.setFare(fairFare);
        assertEquals(fairFare, request.getFare());
        //TODO: will hard code the the correct estimation when possible
    }

    /**
     * US 01.07.01
     As a rider, I want to confirm the completion of a request and enable payment.
     */
    @Test
    public void checkPayment() {
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
        request.setAcceptedStatus(true);
        assertTrue(user.hasAcceptedRequests(request));
    }

    /**
     * US 05.03.01
     * As, a driver I want to see if my acceptance was accepted
     */

    @Test
    public void testAcceptedDriverAcceptance() {
        request.setConfirmedDriver("kf3232");
        assertTrue(request.getRequestStatus().equals(RequestStatus.CONFIRMED));
        assertTrue(request.getConfirmedDriverID().equals("kf3232"));
    }


}
