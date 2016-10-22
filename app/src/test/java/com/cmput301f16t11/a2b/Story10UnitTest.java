package com.cmput301f16t11.a2b;

import org.junit.Test;
import java.net.Authenticator;
import static org.junit.Assert.*;

public class Story10UnitTest {

/* Based on US 02.01.01

   To Test:
   making a new user
*/

    /*
    is this different from createUser? Probably not. Left it for now
    However we should consider removing it.
     */
    User user;
    String userName = "user";
    String passWord = "pass";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10; 

    @Test
    public void testProfileCreation() {

        String newName = UserController.getNewUserName();
        String newPass = UserController.getNewPass();
        String newEmail = UserController.getEmail();


        User user = new User(newName, newPass, newEmail);
        assertEquals(newName, user.getName());
        assertEquals(newPass, user.getPassWord());
        assertEquals(newEmail, user.getEmail());
    }

}