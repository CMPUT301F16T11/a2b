package com.cmput301f16t11.a2b;
import org.junit.Test;
import java.net.Authenticator;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class Story8UnitTest {

/* Based on US 01.08.01
   ToTest:
   Acceptance of a ride once a driver accepts
   Driver Ratings
*/

    User user;
    String userName = "user";
    String passWord = "pass";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10; 

    @Test
    public void testUserAcceptance() {

        user = UserController.loadUser(userName);
        user.createRequest(startLocation, endLocation, fare);
        User user = new User();
        UserRequest request = new UserRequest("bottom", "here", 10);
        assertFalse(user.hasAcceptedRequests(request));
    }

    @Test
    public void testUserAcceptancePending() {

        user = UserController.loadUser(userName);
        user.createRequest(startLocation, endLocation, fare);
        User user = new User();
        UserRequest request = new UserRequest("bottom", "here", 10);
        user.addAcceptedRequest(request);
        assertTrue(user.hasAcceptedRequests(request));
    }


    // think this was meant for a different test
    // @Test
    // public void testDriverRatings() {
    //   Driver driver1 = new driver("Joey Fusion Fallone", 0);
    //   Driver driver2 = new driver("McJesus", 5);
    //   Int rating1 = driver1.getRating();
    //   assertEquals(rating1, 0);
    //   Int rating2 = drver2.getRating();
    //   assertEquals(rating2, 5);
    }