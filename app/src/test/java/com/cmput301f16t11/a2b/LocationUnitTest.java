package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by tothd on 10/13/2016.
 */

public class LocationUnitTest {

    private Number fare = 10;
    //specify a start point
    private LatLng start = new LatLng(50,50);
    //specify an end point
    private LatLng end = new LatLng(50,50);
    private User user = UserController.getUser();
    /**
     * Location
     US 10.01.01
     As a rider, I want to specify a start and end geo locations on a map for a request.
     */
    @Test
    public void testSetStartEndLocations(){

        //create a new request
        UserRequest request = new UserRequest(start,end,fare,user);
        User rider = new User();

        //enter in the start and end points chosen
        request.setStartLocation(start);
        request.setEndLocation(end);

        //add it to rider's list
        rider.addActiveRiderRequest(request);

        assertTrue(request.getStartLocation().equals(new LatLng(50,50)));
        assertTrue(request.getEndLocation().equals(new LatLng(50,50)));
    }

    /**
     US 10.02.01 (added 2016-02-29)

     As a driver, I want to view start and end geo locations on a map for a request.
     */
    @Test
    public void testEndStartEndLocations(){

        UserRequest request = new UserRequest(start,end,fare,user);

        User driver = new User();
        driver.addActiveDriverRequest(request);
        LatLng testStart = driver.getActiveRequestsAsDriver().get(0).getStartLocation();
        LatLng testEnd = driver.getActiveRequestsAsDriver().get(0).getEndLocation();



        assertTrue(testStart.equals(start));
        assertTrue(testEnd.equals(end));

    }


}
