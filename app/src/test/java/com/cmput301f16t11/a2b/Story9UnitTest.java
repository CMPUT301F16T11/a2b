package com.cmput301f16t11.a2b;

import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 Use Case: 9
 ID: US 02.01.01
 Description: A user (rider or driver) would like to view the status of a request that they have either made, accepted or confirmed.
 Primary Actor: The user (rider or driver)
 Supporting Actor(s): N/A Goal: To view the status of a request
 Trigger: The user selects the detail view of a request Pre-conditions:

 At least one request has been made or accepted by the user
 Post-conditions:

 The status of the request is not effected by this action
 Basic Flow:

 1 The (logged in) user selects a button to view current requests involving them
 2 The user selects the request in question
 3 A detail view (activity) is loaded
 Exceptions:

 2 Request not found -> return to 1
 */
public class Story9UnitTest{

    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";

    @Test
    public void check_accepted_true(){
        UserRequest req = new UserRequest(startLocation, endLocation, 10);
        req.setAcceptedStatus(true);
        assertTrue(req.getAcceptedStatus());

    }
    @Test
    public void check_completed_true(){
        UserRequest req = new UserRequest(startLocation, endLocation, 10);
        req.setCompletedStatus(true);
        assertTrue(req.isCompleted());
    }
    @Test
    public void check_accepted_false(){
        UserRequest req = new UserRequest(startLocation, endLocation, 10);
        req.setAcceptedStatus(false);
        assertFalse(req.getAcceptedStatus());
    }
    @Test
    public void check_completed_false(){
        UserRequest req = new UserRequest(startLocation, endLocation, 10);
        req.setCompletedStatus(false);
        assertFalse(req.isCompleted());
    }
}
