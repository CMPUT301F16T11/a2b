package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class StatusUnitTest {


    /*
    This test currently has no tests that are not tested elsewhere. Once we have gui testing it
    may make more sense to test if the list is displaying the correct data
     */

    private LatLng startLocation = new LatLng(50,50);
    private LatLng endLocation = new LatLng(50,50);
    private int fare = 10;
    private User user = UserController.getUser();
    private UserRequest req;
    /**
     *US 02.01.01
     As a rider or driver, I want to see the status of a request that I am involved in
     */
    @Before
    public void setUp(){
        req = new UserRequest(startLocation, endLocation,fare,user);
    }

    @Test
    public void checkAcceptedTrue(){
        req.setAcceptedStatus(true);
        assertTrue(req.getAcceptedStatus());

    }
    @Test
    public void checkCompletedTrue(){
        req.setCompletedStatus(true);
        assertTrue(req.isCompleted());
    }
    @Test
    public void checkAcceptedalse(){
        req.setAcceptedStatus(false);
        assertFalse(req.getAcceptedStatus());
    }
    @Test
    public void checkCompletedFalse(){
        req.setCompletedStatus(false);
        assertFalse(req.isCompleted());
    }
}
