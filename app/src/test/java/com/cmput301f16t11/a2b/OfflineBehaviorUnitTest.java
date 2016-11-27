package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.*;



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

public class OfflineBehaviorUnitTest{
    private LatLng start = new LatLng(53.5443890,-113.4909270);
    private LatLng end = new LatLng(53.5231674,-113.5256026);
    private int fare = 10;
    private User user = new User();
    ArrayList<UserRequest> requestList;
    UserRequest request;
    UserRequest request1;
    UserRequest request2;


    @Before
    public void setup(){
        requestList = new ArrayList<>();
        //build a list of requests
        request = new UserRequest(start,end,fare+1,user.getId());
        request1 = new UserRequest(start,end,fare+2,user.getId());
        request2 = new UserRequest(start,end,fare+3,user.getId());
        //add the requests to the list
        requestList.add(request);
        requestList.add(request1);
        requestList.add(request2);
    }
    /**
    US 08.01.01
    As an driver, I want to see requests that I already accepted while offline.
     */
    @Test
    public void testOfflineAcceptedRequest(){

        //set them to accepted
        request.setAcceptedStatus(true);
        request1.setAcceptedStatus(true);
        request2.setAcceptedStatus(true);
        //push onto command stack

        //grab saved list of requests from the command stack

        //check and see if this list same as given one using test case
        assertTrue(requestList.equals(loadedList));
    }

    /**
     * US 08.02.01
     As a rider, I want to see requests that I have made while offline.
     */
    @Test
    public void testOfflineMadeRequests(){

        //push requestList onto the stack
        SaveLoadController.saveInFile(requestList,"riderOwnRequests.sav");
        //go offline

        //grab saved list of requests from the command stack

        //check that saved list is the same list before going offline
        assertEquals(requestList,loadedList);
    }

    /**
    US 08.03.01
    As a rider, I want to make requests that will be sent once I get connectivity again.
     */
    @Test
    public void testSendMadeRequestsAfterConnection(){
        UserRequest request = new UserRequest(start,end,fare,user.getId());
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
        //grab list of requests
        user.setActiveRequestsAsRider(requestList);
        //accept one of them
        user.getActiveRequestsAsRider().get(0).setAcceptedStatus(true);
        //push this action onto the stack
        //pop this action from the stack once online
        //check that accepted request has been sent
        assertTrue(user.getActiveRequestsAsRider().get(0).getAcceptedStatus());
    }
}
