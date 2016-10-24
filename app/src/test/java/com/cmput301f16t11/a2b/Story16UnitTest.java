package com.cmput301f16t11.a2b;

import org.junit.Test;

import java.net.Authenticator;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class Story16UnitTest {
    /*
        Based on US 05.01.01


        To test:
        Driver accepting request

     */
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10.00;


    @Test
    public void testDriverAcceptingRequest() {

        // random request that Billy wants to accept
        User user = new User();
        UserRequest billyRequest = new UserRequest(startLocation, endLocation, fare);
        user.addAcceptedRequest(billyRequest);

        assertTrue(user.hasAcceptedRequests(billyRequest));
    }

    @Test
    public void testDriverNotAcceptingRequest() {

        // random request that Billy wants to accept
        User user = new User();
        UserRequest billyRequest = new UserRequest(startLocation, endLocation, fare);

        assertFalse(user.hasAcceptedRequests(billyRequest));
    }

}