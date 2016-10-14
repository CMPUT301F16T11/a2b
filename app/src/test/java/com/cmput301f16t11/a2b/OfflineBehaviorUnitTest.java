package com.cmput301f16t11.a2b;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by tothd on 10/12/2016.
 * US 08.01.01
 As an driver, I want to see requests that I already accepted while offline.

 US 08.02.01
 As a rider, I want to see requests that I have made while offline.

 US 08.03.01
 As a rider, I want to make requests that will be sent once I get connectivity again.


 US 08.04.01
 As a driver, I want to accept requests that will be sent once I get connectivity again.
 *
 */

public class OfflineBehaviorUnitTest {

    String username = "username";

    User driver = UserController.loadUser(username);
    String start = "here";
    String end = "there";
    int fare = 10;

    UserRequest request = new UserRequest(start,end,fare);
    /**
    US 08.01.01
    As an driver, I want to see requests that I already accepted while offline.
     */
    @Test
    public void testOfflineAcceptedRequest(){
        //build a list of requests
        UserRequest request = new UserRequest(start,end,fare);
        UserRequest request1 = new UserRequest(start,end,fare);
        UserRequest request2 = new UserRequest(start,end,fare);
        ArrayList<UserRequest> requestList = new ArrayList<>();
        requestList.add(request);
        requestList.add(request1);
        requestList.add(request2);
        //set them to accepted
        request.setAcceptedStatus(true);
        request1.setAcceptedStatus(true);
        request2.setAcceptedStatus(true);
        //save them to a file
        UserController.saveInFile(requestList);
        //set connection to offline
        UserController.setOffline();

        //grab saved list of requests from some file???

        //check and see if this list same as given one using test case
        assertTrue(requestList.equals(UserController.getRequestList()));
    }

    /**
     * US 08.02.01
     As a rider, I want to see requests that I have made while offline.
     */
    @Test
    public void testOfflineMadeRequests(){
        //build a list of requests
        UserRequest request = new UserRequest(start,end,fare);
        UserRequest request1 = new UserRequest(start,end,fare);
        UserRequest request2 = new UserRequest(start,end,fare);
        ArrayList<UserRequest> requestList = new ArrayList<>();
        requestList.add(request);
        requestList.add(request1);
        requestList.add(request2);
        //save them to a file
        UserController.saveInFile(requestList);
        //go offline
        UserController.setOffline();
        //grab saved list of requests
        //check that saved list is the same list before going offline
        assertEquals(requestList,UserController.getRequestList());
    }

    /**
    US 08.03.01
    As a rider, I want to make requests that will be sent once I get connectivity again.
     */
    @Test
    public void testSendMadeRequestsAfterConnection(){
        //go offline
        UserController.setOffline();
        //create a request
        UserRequest request = new UserRequest(start,end,fare);
        //go online
        UserController.goOnline();
        UserController.updateRequestList();
        //check if online list has your created request
        assertTrue(UserController.getRequestList().contains(request));

    }

    /**
     *US 08.04.01
     *As a driver, I want to accept requests that will be sent once I get connectivity again.
     */
    @Test
    public void testAcceptRequestsAfterConnection(){
        //go offline
        UserController.setOffline();
        //grab list of requests
        ArrayList<UserRequest> requestList = UserController.getRequestList();
        UserRequest request = new UserRequest(start,end,fare);
        requestList.add(request);
        //accept one of them
        requestList.get(0).setAcceptedStatus(true);
        //go online
        UserController.goOnline();
        //check that accepted request has been sent
        assertTrue(UserController.getRequestList().get(0).getAcceptedStatus());
    }
}
