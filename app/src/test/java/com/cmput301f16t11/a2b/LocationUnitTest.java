package com.cmput301f16t11.a2b;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by tothd on 10/13/2016.
 */

public class LocationUnitTest {

    private Number fare = 10;
    /**
     * Location
     US 10.01.01
     As a rider, I want to specify a start and end geo locations on a map for a request.
     */
    @Test
    public void testSetStartEndLocations(){
        //specify a start point
        String start = "starting location";
        //specify an end point
        String end = "ending location";
        //create a new request
        UserRequest request = new UserRequest(start,end,fare);
        User rider = new User();

        //enter in the start and end points chosen
        request.setStartLocation("new location");
        request.setEndLocation("home");

        //add it to rider's list
        rider.addRequest(request);

        assertTrue(request.getStartLocation().equals("new location"));
        assertTrue(request.getEndLocation().equals("home"));
    }

    /**
     US 10.02.01 (added 2016-02-29)

     As a driver, I want to view start and end geo locations on a map for a request.
     */
    @Test
    public void testEndStartEndLocations(){
        String start = "next door";
        String end = "west end";
        UserRequest request = new UserRequest(start,end,fare);

        User driver = new User();
        driver.addRequest(request);
        String testStart = driver.getRequests().get(0).getStartLocation();
        String testEnd = driver.getRequests().get(0).getEndLocation();



        assertTrue(testStart.equals(start));
        assertTrue(testEnd.equals(end));

    }


}
