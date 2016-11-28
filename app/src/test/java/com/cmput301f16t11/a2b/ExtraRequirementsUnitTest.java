package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by tothd on 11/24/2016.
 * These unit tests are to
 */

/**
 *
 */
public class ExtraRequirementsUnitTest {

    private User user;
    private UserRequest request;
    private Number fare = 10;
    private LatLng startLocation = new LatLng(53.5443890,-113.4909270);
    private LatLng endLocation = new LatLng(53.5231674,-113.5256026);
    private Vehicle car = new Vehicle("Toyota","Corolla","red",2016);

    @Before
    public void setupTest(){
        user = new User("David","daves@mail.com","7801231234",car);
        request = new UserRequest(startLocation,endLocation,fare,user.getId());
        user.addActiveRiderRequest(request);
        user.addActiveDriverRequest(request);
    }
    /**
     * US 1.09.01 (added 2016-11-14)
     As a rider, I should see a description of the driver's vehicle.
     */
    @Test
    public void testVehicleDescription(){
        assertEquals("red",user.getCar().getColor());
        assertEquals("Toyota",user.getCar().getMake());
        assertEquals("Corolla",user.getCar().getModel());
        assertEquals(2016,user.getCar().getYear());
    }
    /**
     US 1.10.01 (added 2016-11-14)
     As a rider, I want to see some summary rating of the drivers who accepted my offers.
     */
    @Test
    public void testRatingSummary(){
        user.setRating(4.0);
        assertEquals(4.0,user.getRating());

    }

    /**
     US 1.11.01 (added 2016-11-14)
     As a rider, I want to rate a driver for his/her service (1-5).
    */
    @Test
    public void testRateDriver(){
        user.setRating(5.0);
        assertEquals(5.0,user.getRating());}
    /**
     US 03.04.01 (added 2016-11-14)
     As a driver, in my profile I can provide details about the vehicle I drive.
    */
    @Test
    public void testProfileVehicleDescription(){
        assertEquals("red",user.getCar().getColor());
        assertEquals("Toyota",user.getCar().getMake());
        assertEquals("Corolla",user.getCar().getModel());
        assertEquals(2016,user.getCar().getYear());
    }

    /**
     US 04.03.01 (added 2016-11-14)
     As a driver, I should be able to filter request searches by price per KM and price.
    */
    @Test
    public void testFilterRequestSearch(){
        ArrayList<UserRequest> requests = user.getActiveRequestsAsDriver();
        ArrayList<UserRequest> filteredRequests = MockRequestListActivity.getFilteredRequests(requests);
        assertEquals(filteredRequests,requests);

    }
    /**
     US 04.04.01 (added 2016-11-14)
     As a driver, I should be able to see the addresses of the requests.
    */
    @Test
    public void testRequestAddress(){
        LatLng start = request.getStartLocation();
        LatLng end = request.getEndLocation();

        assertTrue(start.equals(user.getActiveRequestsAsDriver().get(0).getStartLocation()));
        assertTrue(end.equals(user.getActiveRequestsAsDriver().get(0).getEndLocation()));
    }
    /**
     US 04.05.01 (added 2016-11-14)
     As a driver, I should be able to search by address or nearby an address.
     */
    @Test
    public void testSearchByAddressOrNearby(){
        //TODO: Not sure how this should be done


    }
}

class MockRequestListActivity {
    private static boolean filterMaxPricePerKM;
    private static boolean filterMaxPrice;
    private static double currentPrice;
    public static ArrayList<UserRequest> getFilteredRequests(ArrayList<UserRequest> listOfRequests) {
        if (UserController.checkMode() == Mode.RIDER) {
            return listOfRequests;
        }
        if (!filterMaxPricePerKM && !filterMaxPrice) {
            return listOfRequests;
        }

        ArrayList<UserRequest> tempList = new ArrayList<UserRequest>();
        if (filterMaxPricePerKM) {
            for (UserRequest request : listOfRequests) {
                if (RequestController.getPricePerKM(request) <= getCurrentPricePerKM()) {
                    tempList.add(request);
                }
            }
        } else if (filterMaxPrice) {
            for (UserRequest request : listOfRequests) {
                if (request.getFare().doubleValue() <= getCurrentPrice()) {
                    tempList.add(request);
                }
            }
        }
        return tempList;
    }

    public static double getCurrentPrice() {
        return currentPrice;
    }

    private static double getCurrentPricePerKM() {
        try {
            return 1;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
