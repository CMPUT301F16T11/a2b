package com.cmput301f16t11.a2b

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story2UnitTest {

/* Based on US 01.02.01
   Jamie had a rough night. She was at a fundraiser for a charitable event 
   but under the stress of her upcoming report due at work, she started to 
   grab a drink or two at the toonie bar. Now she has had far too much to 
   drink, definitely too much to drive home. She usually uses a2b in situations 
   like this, but she cannot for the life of her remember if she already called a 
   car through a2b. Frustrated, she pulls out her phone and pulls up current requests 
   under her account. Jamie is an excellent example of an average a2b user with 
   an average request.

   To Test:
   Jamie logs in
   Check Open requests
*/

   User user;
   String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
   String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
   Number fare = 10.00;
   String userName = "Jamie_SoDrunk";
   String passWord = "oneDrinkTooMany";
   // assuming we might have a class for ride information
   ArrayList<requests> requests = new ArrayList<requests>();

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
    	user = UserController.loadUser(userName);
    	user.createRequest(startLocation,endLocation,fare);
    	requests = user.getLatestActiveRequest()
    	assertEquals(requests.getStartLocation(), startLocation);
    	assertEquals(requests.getEndLocation(), endLocation);
    	assertEquals(requests.getFare(), fare);
    }
}