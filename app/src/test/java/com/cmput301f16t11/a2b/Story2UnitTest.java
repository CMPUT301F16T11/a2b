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

    public void setUp(){
        user = new User();
        user.createRequest(startLocation,endLocation,fare);
        requests = user.getRequests().get(0);
    }

   @Test
   public void user_auth_and_load() throws Exception{

        boolean authenticated = UserController.auth(userName,passWord);
        if(authenticated){
            user = UserController.loadUser(userName);
            assertEquals("Jamie_SoDrunk",user.getName());
        }
        else{
            fail("User not Authenticated");
        }
    }

    @Test
    public void testStartLocation(){

        setUp();
        assertEquals(requests.getStartLocation(), startLocation);
    }

    @Test
    public void testEndLocation(){

        setUp();
        assertEquals(requests.getEndLocation(), endLocation);
    }

    @Test
    public void testFare(){

        setUp();
        assertEquals(requests.getFare(), fare);
    }
}