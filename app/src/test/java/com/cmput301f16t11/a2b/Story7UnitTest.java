package com.cmput301f16t11.a2b;

import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 Use Case: 7
 ID: US 01.07.01
 Description: A rider confirms that a request has been completed and officially transfer payment.
 Primary Actor: The casual rider
 Supporting Actor(s): The driver who has completed the rider's request
 Goal: For the rider to close a request as completed and complete payment to the driver
 Pre-conditions:

 Driver has accepted and completed the rider's request
 The rider had confirmed the acceptance
 Post-conditions:

 The request is closed as completed, and the rider's payment information is processed.
 Basic Flow:

 1 The (logged in) rider selects a confirm that they have arrived at the destination
 2 The rider selects the payment option
 Exceptions:

 2 Valid Payment information not found -> input payment information
 */
public class Story7UnitTest extends TestCase {
    String userName = "jamie";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10.00;

    User rider = UserController.loadUser(userName);
    User driver = UserController.loadUser("billy");
    UserRequest jamieRequest;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        rider.createRequest(startLocation,endLocation,fare);
        jamieRequest = rider.getLatestActiveRequest();
        driver.addAcceptedRequest(jamieRequest);
        jamieRequest.setAcceptedStatus(true); // rider accepts ride
    }


    @Test
    public void check_completion() throws Exception{

        jamieRequest.setCompletedStatus(true); // driver completes ride
        assertEquals(true,jamieRequest.isCompleted());

    }

    @Test
    public void check_payment() throws Exception{

        jamieRequest.setPaymentReceived(true); // rider pays
        assertEquals(true,jamieRequest.isPaymentRecived());
    }


}
