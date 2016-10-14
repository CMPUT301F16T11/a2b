
package com.cmput301f16t11.a2b;

import org.junit.Test;

import static org.junit.Assert.*;

public class Story6UnitTest {

/* Based on US 01.06.01
   
   ToTest:
   Fare Estimation
*/

    User user;
    String username = "Raine";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 0;

    public void setUp(){

        user = UserController.loadUser(username);
        user.createRequest(startLocation, endLocation, fare);
    }

    @Test
    public void testFareEstimation() {
        setUp();

        UserRequest request = user.getLatestActiveRequest();
        fare = request.getFareEstimation(startLocation, endLocation);
        assertEquals(15, fare); // will hard code the the correct estimation when
    }

}
