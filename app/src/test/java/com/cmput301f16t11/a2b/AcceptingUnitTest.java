package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tothd on 10/24/2016.
 */

public class AcceptingUnitTest {


    private User driver =  UserController.getUser();
    private LatLng startLocation = new LatLng(50,50);
    private LatLng endLocation = new LatLng(50,50);
    private Number fare = 10.00;


    /**
     * US 05.02.01
<<<<<<< HEAD
     As a driver, I want to view a list of things I have accepted that are pending,
     each request with its description, and locations.
     */
    @Test
    public void testDriverAcceptingRequest() {

        // random request that Billy wants to accept
        User user = new User();
        UserRequest billyRequest = new UserRequest(startLocation, endLocation, fare, driver);
        user.addDriverRequest(billyRequest);
        assertTrue(user.getActiveRequestsAsDriver().size() > 0);
        user.addActiveDriverRequest(billyRequest);
        assertTrue(user.hasAcceptedRequests(billyRequest));
    }

    @Test
    public void testDriverNotAcceptingRequest() {

        // random request that Billy wants to accept
        User user = new User();
        UserRequest billyRequest = new UserRequest(startLocation, endLocation, fare, driver);
        user.addDriverRequest(billyRequest);
        assertTrue(user.getActiveRequestsAsDriver().size() > 0);
        assertFalse(user.hasAcceptedRequests(billyRequest));
    }


    /**
     * US 05.04.01
     As a driver, I want to be notified if my ride offer was accepted.
     */


    @Test
    public void testNotificationOfferAccepted(){
        UserRequest request = new UserRequest(startLocation,endLocation,fare,driver);
        //get the list of requests
        ArrayList<UserRequest> requestList = driver.getActiveRequestsAsDriver();
        //check if any are accepted
        for(UserRequest r: requestList)
            if(request.getAcceptedStatus()){
                //if there are any accepted send notification
                driver.notifyUser(r);
                request = r;
            }

        //test if notification was sent
        assertEquals(request.sentNotification(),true);

    }

}

//    @Test
//    public void testDriverAcceptingRequest() {
//
//        // random request that Billy wants to accept
//        User user = new User();
//        UserRequest billyRequest = new UserRequest(startLocation, endLocation, fare);
//        user.addActiveDriverRequest(billyRequest);
//        assertTrue(user.getActiveRequestsAsDriver().size() > 0);
//        assertTrue(user.hasAcceptedRequests(billyRequest));
//    }


//TODO: Fix tests such that they comply with new User modelling
//
//    @Test
//    public void testDriverNotAcceptingRequest() {
//
//        // random request that Billy wants to accept
//        User user = new User();
//        UserRequest billyRequest = new UserRequest(startLocation, endLocation, fare);
//        user.addRequest(billyRequest);
//        assertTrue(user.getRequests().size() > 0);
//        assertFalse(user.hasAcceptedRequests(billyRequest));
//    }
//
//
//    /**
//     * US 05.04.01
//     As a driver, I want to be notified if my ride offer was accepted.
//     */
//
//
//    @Test
//    public void testNotificationOfferAccepted(){
//        UserRequest request = new UserRequest(startLocation,endLocation,fare);
//        //get the list of requests
//        ArrayList<UserRequest> requestList = driver.getRequests();
//        //check if any are accepted
//        for(UserRequest r: requestList)
//            if(request.getAcceptedStatus()){
//                //if there are any accepted send notification
//                driver.notifyUser(r);
//                request = r;
//            }
//
//        //test if notification was sent
//        assertEquals(request.sentNotification(),true);
//
//    }
//
//}

