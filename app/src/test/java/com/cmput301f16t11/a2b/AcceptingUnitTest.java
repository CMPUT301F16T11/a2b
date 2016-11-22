package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;



import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by tothd on 10/24/2016.
 */

public class AcceptingUnitTest {
    private User user;
    private UserRequest request;
    private LatLng startLocation = new LatLng(50,50);
    private LatLng endLocation = new LatLng(50,50);
    private Number fare = 10.00;


    @Before
    public void setup(){
        user = new User();
        request = new UserRequest(startLocation,endLocation,fare,user.getId());
    }
    /**
     * US 05.01.01
     As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
     */

    @Test
    public void testDriverAcceptingRequest() {

        // random request that is accepted
        user.addActiveDriverRequest(request);
        assertTrue(user.getActiveRequestsAsDriver().size() > 0);

        user.getActiveRequestsAsDriver().get(0).setAcceptedStatus(true);
        assertTrue(request.getAcceptedStatus());

        //TODO: accept offered payment upon completion
    }

    @Test
    public void testDriverNotAcceptingRequest() {
        // request that is not accepted
        user.addActiveDriverRequest(request);
        assertTrue(user.getActiveRequestsAsDriver().size() > 0);

        assertFalse(user.hasAcceptedRequests(request));
    }
    /**
     * US 05.02.01
     As a driver, I want to view a list of things I have accepted that are pending,
     each request with its description, and locations.
     */
    @Test
    public void testViewAcceptedRequestDescriptionLocation(){
        request.setAcceptedStatus(true);
        user.addActiveDriverRequest(request);

        UserRequest req2 = new UserRequest(new LatLng (23,23),new LatLng(33,33),11.0,user.getId());
        req2.setAcceptedStatus(true);

        user.addActiveDriverRequest(req2);

        ArrayList<UserRequest> reqList = user.getActiveRequestsAsDriver();

        assertEquals(reqList.get(0), request);
        assertEquals(reqList.get(1), req2);


    }


    /**
     * US 05.04.01
     As a driver, I want to be notified if my ride offer was accepted.
     */

    @Test
    public void testNotificationOfferAccepted(){
        user.addActiveDriverRequest(request);
        //get the list of requests
        ArrayList<UserRequest> requestList = user.getActiveRequestsAsDriver();
        requestList.get(0).setAcceptedStatus(true);

        //check if any are accepted
        for(UserRequest r: requestList)
            if(request.getAcceptedStatus()){
                //if there are any accepted send notification
                //driver.notifyUser(r); TODO: create working notifyUser method
                request = r;
            }

        //test if notification was sent
        assertEquals(request.sentNotification(),true);

    }



}

