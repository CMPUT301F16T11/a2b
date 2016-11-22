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

    private User rider1 =  new User("rider1","r1@email.com"); // rider
    private User rider2 =  new User("rider2","r2@email.com"); // rider
    private User rider3 =  new User("rider3","r3@email.com"); // rider
    private User rider4 =  new User("rider4","r4@email.com"); // rider

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

        req1 = new UserRequest(startLocation1,endLocation1,10.00,rider1.getId());
        req2 = new UserRequest(startLocation2,endLocation2,10.00,rider2.getId());
        req3 = new UserRequest(startLocation3,endLocation3,10.00,rider3.getId());
        req4 = new UserRequest(startLocation4,endLocation4,10.00,rider4.getId());

        rider1.addActiveRiderRequest(req1);
        rider2.addActiveRiderRequest(req2);
        rider3.addActiveRiderRequest(req3);
        rider4.addActiveRiderRequest(req4);

    }

    /**
     * US 04.01.01
     As a driver, I want to browse and search for open requests by geo-location.
     */
    @Test
    public void checkListOfNearbyRequests(){
        ArrayList<UserRequest> nearbyRequests = RequestController.getNearbyRequests();
        nearbyRequests.add(req1);
        nearbyRequests.add(req2);
        nearbyRequests.add(req3);
        nearbyRequests.add(req4);
        assertEquals(4, nearbyRequests.size());
    }

    /**
     * US 04.02.01
     As a driver, I want to browse and search for open requests by keyword.
     */

    @Test
    public void testGetRequests() {

        //TODO: not searching by keyword, need to add this functionality
        User user1 = new User();
        User user2 = new User();

        UserRequest request1 = new UserRequest(new LatLng(51,51), new LatLng(50,50), 123, user1.getId());
        UserRequest request2 = new UserRequest(new LatLng(52,52), new LatLng(53,53), 23, user2.getId());
        user1.addActiveDriverRequest(request1);
        user2.addActiveDriverRequest(request2);

        ArrayList<UserRequest> retrieve1 = user1.getActiveRequestsAsDriver();
        ArrayList<UserRequest> retrieve2 = user2.getActiveRequestsAsDriver();
        assertEquals(retrieve1.get(0), request1);
        assertEquals(retrieve2.get(0), request2);



    }
}
