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
        Based on 05.01.01
Billy sees another request he likes, with a fare he thinks is reasonable. He finishes 
his donut and accepts the request. Billy is an excellent example of an average driver 
making an average request.

To test:
Driver accepting request

     */

    User user;
    String userName = "billy";
    String passWord = "billy1zth3b35t";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10.00;


    @Test
    public void testDriverAcceptingRequest() {

        user = UserController.loadUser(userName);
        ArrayList<UserRequest> requests = user.getAllRequests();
        UserRequest request = requests.get(3); // random request that Billy wants to accept
        Driver driver = new Driver("Joey Fusion Fallone", 0);
        ArrayList<UserRequest> requests2 = driver.getAllRequests();
        UserRequest request2 = requests.get(0);
        driver.acceptRequest(request2);
        assertTrue(driver.hasAccepted(request2));
    }

}