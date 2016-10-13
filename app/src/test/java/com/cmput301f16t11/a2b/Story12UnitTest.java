package com.cmput301f16t11.a2b;

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story12UnitTest {

/* Based on US 03.02.01
Billy is doing really well for himself. He has so much money now that he 
decides to set up his own private email server. After failing miserably 
he decides to just make a new account online. He changes his profile on 
a2b to reflect this change in his contact information. Billy is an excellent 
of an average rider or driver, making an average request.

To Test:
changing User info
*/

    @Test
    public void testChangeInfo() {
      User user = new User("BallingBilly", "7805559862", "IhaveSoMuchCash@ualberta.ca", "88510 105 street")
      user.changeName("Billy is dumb");
      assertEquals("Billy is dumb", user.getName());
      user.changePhoneNum("7801234567");
      assertEquals("7801234567", user.getPhone());
      user.changeEmail("test@ualberta.ca");
      assertEquals("test@ualberta.ca", user.getEmail());
    }