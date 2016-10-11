package com.example.liangkelvin.a2btests

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story8UnitTest {

/* Based on US 01.08.01
   Meeri is in a highly populated part of the city at a time when not 
   many people need rides. She requests an a2b ride and notices that 
   multiple drivers have accepted. She checks which driver has the 
   highest rating and confirms that acceptance. Meeri is an excellent 
   example of an average user making an average interaction with a2b.

   ToTest:
   Driver Ratings
*/

    User user;
    String userName = "Meeri";
    String passWord = "whatKindOfPersonIsNamedMeeri";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10; 

    @Test
    public void testDriverRatings() {
      Driver driver1 = new driver("Joey Fusion Fallone", 0);
      Driver driver2 = new driver("McJesus", 5);
      Int rating1 = driver1.getRating();
      assertEquals(rating1, 0);
      Int rating2 = drver2.getRating();
      assertEquals(rating2, 5);

    }