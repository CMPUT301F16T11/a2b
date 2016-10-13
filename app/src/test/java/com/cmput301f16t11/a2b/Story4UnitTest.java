package com.cmput301f16t11.a2b

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story4UnitTest {

/* Based on US 01.04.01

   To Test:
   delete A request
*/

    User user;
    String userName = "billy";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10.00;

    @Test
    public void testDeleteRequest() {
      user = UserController.loadUser(userName);
      user.createRequest(startLocation, endLocation, fare);
      assertEquals(1, user.numberOfActiveRequests());
      user.removeRequest(user.getLatestRequest());
      assertEquals(0, user.numberOfActiveRequests());
    }
 }