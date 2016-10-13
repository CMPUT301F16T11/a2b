package com.cmput301f16t11.a2b;

import org.junit.Test;
import java.net.Authenticator;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class Story2UnitTest {

/* Based on US 01.02.01

   To Test:
   Logging in
   Check Open requests
*/

   User user;
   String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
   String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
   Number fare = 10.00;
   String userName = "Jamie_SoDrunk";
   String passWord = "oneDrinkTooMany";
   // assuming we might have a class for ride information
  UserRequest requests;

   @Test
   public void user_auth_and_load() throws Exception{

        boolean authenticated = UserController.auth(userName,passWord);
        if(authenticated){
            user = UserController.loadUser(userName);
            assertEquals("Jamie_SoDrunk",user.getName());
        }else{
            fail("User not Authenticated");
        }
    }

    // this tests that the request info can be retrieved properly
    // don't know how I should test if it displays
    @Test
    public void testCheckRequests() {
        String startLocation2 = "12345 123 st Nw EDmonton";
        String endLocation2 = "54321 321 st Nw edmonton";
        Number fare2 = 20;
    	user = UserController.loadUser(userName);
    	user.createRequest(startLocation,endLocation,fare);
        user.createRequest(startLocation2, endLocation2, fare2);
    	requests = user.getAllRequests().get(0);
        assertEquals(requests.getStartLocation(), startLocation);
        assertEquals(requests.getEndLocation(), endLocation);
        assertEquals(requests.getFare(), fare);
        UserRequest requests2 = user.getAllRequests().get(1);
        assertEquals(requests2.getStartLocation(), startLocation2);
        assertEquals(requests2.getEndLocation(), endLocation2);
        assertEquals(requests2.getFare(), fare2);

    }
}