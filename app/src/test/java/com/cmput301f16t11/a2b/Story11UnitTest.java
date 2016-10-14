package com.cmput301f16t11.a2b;

import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 Use Case: 11
 ID: US 03.02.01
 Description: A user would like to edit the contact information in their profile
 Primary Actor: The user (rider or driver)
 Supporting Actor(s): N/A
 Goal: To edit the profile for the user
 Trigger: The user hits the "edit" button in "myprofile"
 Pre-conditions:

 Connection to the internet
 The user is logged in
 The user is in the "myprofile" view
 Post-conditions:

 The account information is updated
 Basic Flow:

 1 The user views the main page
 2 The user selects my profile button
 3 The user selects the edit button
 4 The user is prompted to edit contact info (phone number, email)
 5 The user's changes are recorded upon hitting "save"
 6 Return to main screen
 Exceptions:

 4 Invalid contact info -> Return to 2 after notifying the user
 5 Changes could not be saved -> Notify the user and return to 2

 */
public class Story11UnitTest{

    User rider =  UserController.loadUser("some1"); // rider
    String oldNumber = "780-888-9999";
    String oldEmail = "buddy@yahoo.com";
    String newNumber = "780-666-9999";
    String newEmail = "buddy@gmail.com";


    private void setUp() {
        rider.setEmail(oldEmail);
        rider.setPhoneNumber(oldNumber);
    }

    @Test
    public void check_email_change(){
        setUp();
        rider.setEmail(newEmail);
        assertEquals(newEmail,rider.getEmail());

    }

    @Test
    public void check_number_change() {
        setUp();
        rider.setPhoneNumber(newNumber);
        assertEquals(newNumber,rider.getPhoneNumber());

    }
}
