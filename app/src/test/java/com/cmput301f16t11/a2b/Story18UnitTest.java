package com.cmput301f16t11.a2b;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tothd on 10/12/2016.
 * Based on the case
 * US 05.04.01
 As a driver, I want to be notified if my ride offer was accepted.


 */

public class Story18UnitTest {
    String username = "username";

    User driver = UserController.loadUser(username);
    String start = "here";
    String end = "there";
    int fare = 10;


    public void testNotificationOfferAccepted(){
        UserRequest request = new UserRequest(start,end,fare);
        //get the list of requests
        ArrayList<UserRequest> requestList = driver.getRequests();
        //check if any are accepted
        for(UserRequest r: requestList)
        if(r.getAccepted()){
            //if there are any accepted send notification
            driver.notifyUser(r);
            request = r;
        }

        //test if notification was sent
        assertEquals(request.sentNotification(),true);

    }
}
