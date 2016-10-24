package com.cmput301f16t11.a2b;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by tothd on 10/24/2016.
 */

public class UserProfileUnitTest {
    /**
    User profile
    */
    private User user; 
    private String oldNumber = "780-888-9999";
    private String oldEmail = "user@domain.com";


    @Before
    private void setUp() {
        user =  UserController.loadUser("some1");
        user.setEmail(oldEmail);
        user.setPhoneNumber(oldNumber);
    }
    /**
    US 03.01.01
    As a user, I want a profile with a unique username and my contact information.
    */
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
     /**
    US 03.02.01
    As a user, I want to edit the contact information in my profile.
    */


    @Test
    public void checkEmailChange(){
        String newEmail = "user@domain2.com";
        user.setEmail(newEmail);
        assertEquals(newEmail,user.getEmail());

    }

    @Test
    public void checkNumberChange() {
        String newNumber = "780-666-9999";
        user.setPhoneNumber(newNumber);
        assertEquals(newNumber,user.getPhoneNumber());

    }
    /**
     US 03.03.01
     As a user, I want to, when a username is presented for a thing, retrieve and show its contact information.
     */
    @Test
    public void testChangeInfo() {
        user = new User("user", "password", "user@ualberta.ca");
        user.setName("name1");
        assertEquals("name1", user.getName());
        user.setPassWord("7801234567");
        assertEquals("7801234567", user.getPassWord());
        user.setEmail("test@ualberta.ca");
        assertEquals("test@ualberta.ca", user.getEmail());
    }
}
