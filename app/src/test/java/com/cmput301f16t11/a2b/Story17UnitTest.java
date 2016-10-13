package com.cmput301f16t11.a2b

import org.junit.Test;
import java.net.Authenticator;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class Story17UnitTest {

/* Based on US 05.03.01
As a driver, I want to see if my acceptance was accepted.
*/

    User driver;
    String username = "username";
    String start = "here";
    String end = "there";
    int fare = 10;

    @Test
    public void testConfirmAccepted() {
        //Login as driver
        driver = UserController.loadUser(username);
        //Look into list of pending accepted requests
        ArrayList<UserRequest> requests = driver.getRequests();
        UserRequest request = new UserRequest(start,end,fare);
        request.setAccepted(true);
        requests.add(request);

        //see that a pending request has been accepted
        assertEquals(request.getAccepted(), true);
        //Assertequals on the wanted vs. actual

    }

}