package com.cmput301f16t11.a2b;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by tothd on 10/24/2016.
 */

public class SearchingUnitTest {
    private User rider1 =  UserController.loadUser("rider1"); // rider
    private User rider2 =  UserController.loadUser("rider2"); // rider
    private User rider3 =  UserController.loadUser("rider3"); // rider
    private User rider4 =  UserController.loadUser("rider4"); // rider

    private User user = UserController.loadUser("billy"); // driver

    private String userName = "user";
    private String startLocation1 = "8210 108 St NW Edmonton, AB T6E 5T2";
    private String endLocation1 = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    private String startLocation2 = "8210 106 St NW Edmonton, AB T6E 5T2";
    private String endLocation2 = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    private String startLocation3 = "8210 109 St NW Edmonton, AB T6E 5T2";
    private String endLocation3 = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    private String startLocation4 = "8210 110 St NW Edmonton, AB T6E 5T2";
    private String endLocation4 = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";

    private UserRequest req1;
    private UserRequest req2;
    private UserRequest req3;
    private UserRequest req4;

    @Before
    public void setUp() {
        rider1.createRequest(startLocation1,endLocation1,10.00);
        rider2.createRequest(startLocation2,endLocation2,10.00);
        rider3.createRequest(startLocation3,endLocation3,10.00);
        rider4.createRequest(startLocation4,endLocation4,10.00);
        req1 = rider1.getLatestActiveRequest();
        req2 = rider2.getLatestActiveRequest();
        req3 = rider3.getLatestActiveRequest();
        req4 = rider4.getLatestActiveRequest();
    }

    /**
     * US 04.01.01
     As a driver, I want to browse and search for open requests by geo-location.
     */
    @Test
    public void checkListOfNearbyRequests(){
        ArrayList<UserRequest> nearbyRequests = RequestController.getRequestNear(startLocation1,20);
        assertEquals(4, nearbyRequests.size());
    }

    /**
     * US 04.02.01
     As a driver, I want to browse and search for open requests by keyword.
     */

    @Test
    public void testGetRequests() {
        user = UserController.loadUser(userName);
        UserRequest request = new UserRequest("10025 125 st", "12586 58 ave", 123);
        UserRequest request2 = new UserRequest("1212 32 st", "12322 43 ave", 23);
        user.addRequest(request);
        user.addRequest(request2);

        ArrayList<UserRequest> retrieve = user.getRequests();
        assertEquals(retrieve.get(0), request);
        assertEquals(retrieve.get(1), request2);
    }
}
