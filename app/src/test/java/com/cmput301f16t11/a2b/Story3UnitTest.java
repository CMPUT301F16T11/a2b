package com.cmput301f16t11.a2b;

import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 Use Case: 3
 Id: US 01.03.01
 Description: A casual rider should be notified of whether or not his request has been accepted in real time.
 Primary Actor: The casual rider
 Supporting Actor(s): Driver(s) who have accepted the request
 Goal: To immediately notify the rider of an accepted request when a driver chooses to do so
 Pre-conditions:

 Rider has submitted at least one request
 At least one driver has accepted said request
 Post-conditions:

 The rider has been notified of the acceptance over the standard android notification system
 Basic Flow:

 1 A notification appears on the rider's device
 Exceptions:

 If the rider is not connected to the internet
 The data should be stored and sent as soon as the device connects
 */
public class Story3UnitTest{
    String userName = "jamie";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10.00;
    User rider = UserController.loadUser(userName);
    User driver = UserController.loadUser("Ryan Gosling from Drive");
    UserRequest jamieRequest;

    private void setUp()
    {
        rider.createRequest(startLocation,endLocation,fare);
        jamieRequest = rider.getLatestActiveRequest();
    }

    @Test
    public void send_and_recive_Notification(){

        setUp();
        driver.addAcceptedRequest(jamieRequest);
        driver.notifyUser(jamieRequest);
        assertTrue(jamieRequest.sentNotification());

    }
}
