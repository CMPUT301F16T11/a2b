package com.cmput301f16t11.a2b

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story10UnitTest {

/* Based on US 02.01.01
   Jamie missed her bus and wants to be home on time for the first episode of "Luke Cage." 
   She realises that "Luke Cage" is a streaming original, however her roommate insisted 
   she would start watching without her if she did not get home by 6. Frantically she pulls 
   out her phone and requests an a2b. She notices that the driver she had the night before 
   has accepted her request after checking the status of her requests. Happily she confirms 
   the acceptance. Riding with Billy had been like being in a limo! Jamie is an excellent 
   example of an average user making an average request.
*/

    User user;
    String userName = "JamieLovesLukeCage";
    String passWord = "JamieIsMissingLukeCage";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10; 

    @Test
    public void testConfirmRequest() {
      user = UserController.getUser(userName);
      user.createRequest(startLocation, endLocation, fare);
      user userDriver = UserController.getUser("JamiesFaveDriver");
      Request request = user.getLatestRideRequests();
      request.confirmRequest();
      assertEquals(request.isConfirmed(), true);

    }