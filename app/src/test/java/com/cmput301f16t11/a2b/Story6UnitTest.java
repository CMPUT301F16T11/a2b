
package com.cmput301f16t11.a2b

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story6UnitTest {

/* Based on US 01.06.01
   Raine is an extremely successful real estate agent. Part of her 
   success comes from arranging rides for clients who do not have 
   their own transportation to prospective properties. While she 
   can usually drive her clients, sometimes she gets them to use an 
   app like a2b to pick them up on the promise of later reimbursement. 
   She needs to see which service generally offers the most reasonable 
   fares. She brings up a2b and tests a few different locations for 
   trips for a fair fare estimate on each. Raine is an example of a 
   power user, but she is using functionality that an average user would 
   also use.
   ToTest:
*/

    User user;
    String username = "Raine";
    String passWord = "IamAPowerUser";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 0;

    @Test
    public void testFareEstimation() {
        user = UserController.loadUser(username);
        user.createRequest(startLocation, endLocation, fare);
        UserRequest request = user.getLatestActiveRequest();
        fare = request.getFareEstimation();
        assertTrue(fare > 0); // will instead put an estimation to check when fareEstimate algorithim is done
        request.setEndLocation("888 91 St NW, Edmonton, AB, T6R 2N5");
        Number fare2 = request.getFareEstimation(startLocation,endLocation);
        assertTrue(fare2 > 0);
        assertTrue(fare != fare2);
    }

}
