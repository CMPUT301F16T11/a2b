package com.cmput301f16t11.a2b

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story17UnitTest {

/* Based on US 05.03.01
As a driver, I want to see if my acceptance was accepted.
*/

    Driver driver;
    String userName = "username";
    String password = "password";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10; 

    @Test
    public void testConfirmRequest() {
      user = UserController.getUser(userName);
      user.createRequest(startLocation, endLocation, fare);
      user userDriver = UserController.getUser("JamiesFaveDriver");
      Request request = user.getLatestRideRequests();
      request.confirmRequest();
      assertEquals(request.isConfirmed(), true);

    }