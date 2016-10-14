package com.cmput301f16t11.a2b;

import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 Use Case: 5
 Id: US 01.05.01
 Description: A casual rider would like to get in contact with a driver who has accepted one of their requests, either by phone number or by email.
 Primary Actor: The casual rider
 Supporting Actor(s): The driver who has accepted the rider's request
 Goal: To provide the contact information of a driver who has accepted a rider's request to said rider
 Pre-conditions:

 App has access to the internet
 At least one request has been made by the rider
 The driver has accepted at least one request made by the rider
 Post-conditions:

 The driver's contact information is presented to the rider in a clear, readable fashion
 Basic Flow:

 1 The (logged in) rider selects a button to view current accepted requests
 2 The rider selects an accepted request
 3 The rider selects a button to view details about the driver who accepted the request
 4 The contact information of the driver is presented to the rider
 Exceptions:

 Contact information not found
 In this case the system should make an immediate note as all users should have signed up with contact information
 Internet connection not working
 The rider should be notified that their connection is not working and given the opportunity to attempt the operation again
 */
public class Story5UnitTest extends TestCase {
    User rider = UserController.loadUser("jamie");
    User driver = UserController.loadUser("Ryan Gosling from Drive");

    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";

    String newDriverPhoneNumber = "780-666-0000";
    String newDriverEmail = "buddy@gmail.com";
    Number fare = 10.00;


    UserRequest jamieRequest;
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        driver.setEmail(newDriverEmail);
        driver.setPhoneNumber(newDriverPhoneNumber);

        rider.createRequest(startLocation,endLocation,fare);
        UserRequest jamieRequest = rider.getLatestActiveRequest();
        driver.addAcceptedRequest(jamieRequest);
        jamieRequest.setAccepted(true);


    }

    @Test
    public void get_driver_phonenumber() throws Exception{
        // To do: get driver from request
        assertEquals(newDriverPhoneNumber, driver.getPhoneNumber());
    }

    @Test
    public void get_driver_email() throws Exception{
        // To do: get driver from request
        assertEquals(newDriverEmail, driver.getEmail());
    }
}
