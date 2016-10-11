package com.example.liangkelvin.a2btests

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story4UnitTest {

/* Based on US 01.04.01
   Billy is frustrated. He is still waiting for his a2b car to arrive. 
   He knows that if he misses this meeting it will be the third time this 
   month he has missed such an event and is likely to be fired. He runs
   back into his car and in desperation keeps turning the ignition slowly 
   watching his hopes die as the engine clicked in response. Finally, out of
   nowhere, the engine sputters to life as he turns the key. Billy is excited,
   but not so excited that he forgets to cancel his a2b. Before he shifts 
   his car out of park (distracted driving is bad after all), he loads a2b on 
   his phone and cancels his previous driving request. There may or may not be 
   a fee associated with this late cancellation. Billy is an excellent example 
   of an average a2b user with an average request.

   To Test:
   delete A request
*/

    User user;
    String userName = "billy";
    String passWord = "billy1zth3b35t";
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