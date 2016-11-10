package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by tothd on 10/24/2016.
 */

public class SearchingUnitTest {

    private User rider1 =  new User(); // rider
    private User rider2 =  new User(); // rider
    private User rider3 =  new User(); // rider
    private User rider4 =  new User(); // rider

    private LatLng startLocation1 = new LatLng(50,50);
    private LatLng endLocation1 = new LatLng(50,50);
    private LatLng startLocation2 = new LatLng(50,50);
    private LatLng endLocation2 = new LatLng(50,50);
    private LatLng startLocation3 = new LatLng(50,50);
    private LatLng endLocation3 = new LatLng(50,50);
    private LatLng startLocation4 = new LatLng(50,50);
    private LatLng endLocation4 = new LatLng(50,50);

    private UserRequest req1;
    private UserRequest req2;
    private UserRequest req3;
    private UserRequest req4;

    @Before
    public void setUp() {
        rider1.addActiveRiderRequest(new UserRequest(startLocation1,endLocation1,10.00,rider1));
        rider2.addActiveRiderRequest(new UserRequest(startLocation2,endLocation2,10.00,rider2));
        rider3.addActiveRiderRequest(new UserRequest(startLocation3,endLocation3,10.00,rider3));
        rider4.addActiveRiderRequest(new UserRequest(startLocation4,endLocation4,10.00,rider4));
        req1 = rider1.getActiveRequestsAsRider().get(0);
        req2 = rider2.getActiveRequestsAsRider().get(0);
        req3 = rider3.getActiveRequestsAsRider().get(0);
        req4 = rider4.getActiveRequestsAsRider().get(0);
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
        //TODO: not searching by keyword, need to add this functionality
        User user1 = UserController.getUser();
        User user2 = new User();

        UserRequest request1 = new UserRequest(new LatLng(51,51), new LatLng(50,50), 123, user1);
        UserRequest request2 = new UserRequest(new LatLng(52,52), new LatLng(53,53), 23, user2);
        user1.addActiveDriverRequest(request1);
        user2.addActiveDriverRequest(request2);

        ArrayList<UserRequest> retrieve1 = user1.getActiveRequestsAsDriver();
        ArrayList<UserRequest> retrieve2 = user2.getActiveRequestsAsDriver();
        assertEquals(retrieve1.get(0), request1);
        assertEquals(retrieve2.get(1), request2);
    }
}
