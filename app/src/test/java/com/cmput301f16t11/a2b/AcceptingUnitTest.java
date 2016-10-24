package com.cmput301f16t11.a2b;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tothd on 10/24/2016.
 */

public class AcceptingUnitTest {
    User rider =  UserController.loadUser("some1"); // rider
    User driver =  UserController.loadUser("somedriver"); // rider
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    UserRequest req;
    Number fare = 10.00;
    String username = "username";


    /*
    cannot find any non duplicate tests here. is this supposed to be offline?
     */

    /**
     * US 05.01.01
     As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
     */
//    private void setUp() throws Exception {
//        rider.createRequest(startLocation,endLocation,10.00);
//        req = rider.getLatestActiveRequest();
//
//    }
//
//    @Test
//    public void testDriverAccept() throws Exception {
//        setUp();
//        driver.addAcceptedRequest(req);
//        assertEquals(1,driver.getAcceptedRequests().size());
//    }
//
//    @Test
//    public void testRiderAcceptance() throws Exception {
//        setUp();
//        req.setAcceptedStatus(true);
//        assertTrue(req.getAcceptedStatus());
//    }
//
//    @Test
//    public void testDriverComplete() throws Exception {
//        setUp();
//        req.setCompletedStatus(true);
//        assertTrue(req.isCompleted());
//    }
//
//    @Test
//    public void testRiderPaymentComplete() throws Exception {
//        setUp();
//        req.setPaymentReceived(true);
//        assertTrue(req.isPaymentRecived());
//    }



    /**
     * US 05.02.01
     As a driver, I want to view a list of things I have accepted that are pending, each request with its description, and locations.
     */
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

    /**
     * US 05.03.01
     As a driver, I want to see if my acceptance was accepted.
     */
     /* Based on US 05.03.01
    As a driver, I want to see if my acceptance was accepted.
    */

    /*
    see test story 8.
    gui tests? - "see"
     */

//    User driver;
//    String username = "username";
//    String start = "here";
//    String end = "there";
//    int fare = 10;
//
//    @Test
//    public void testConfirmAccepted() {
//        //Login as driver
//        driver = UserController.loadUser(username);
//        //Look into list of pending accepted requests
//        ArrayList<UserRequest> requestList = driver.getRequests();
//        UserRequest request = new UserRequest(start,end,fare);
//        request.setAcceptedStatus(true);
//        requestList.add(request);
//
//        //see that a pending request has been accepted
//        assertEquals(request.getAcceptedStatus(), true);
//        //assertequals on the wanted vs. actual
//
//    }
    /**
     * US 05.04.01
     As a driver, I want to be notified if my ride offer was accepted.
     */


    @Test
    public void testNotificationOfferAccepted(){
        UserRequest request = new UserRequest(startLocation,endLocation,fare);
        //get the list of requests
        ArrayList<UserRequest> requestList = driver.getRequests();
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
