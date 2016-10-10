package com.example.brianofrim.a2btests;

import org.junit.Test;

import java.net.Authenticator;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class Story1UnitTest {
    /*
        Based on US 01.01.01
        Billy is in a rush. It's -30 degree Celsuis outside and his car won't start because
        he left the radio on overnight. He has a big meeting in about thirty minutes and no
        time to waste. He remembers that last night he downloaded and signed up for the next
        "big thing app," at the request of his friends. He loads up a2b on his phone and requests
        a ride from his current location, his house, to his workplace. Billy is an excellent example
        of an average a2b user with an average request.

         Testable items:
            Billy login
            Open new request
            Enter start location : home
            Enter end location : work
            Enter fare
     */

    User user;
    String userName = "billy";
    String passWord = "billy1zth3b35t";
    String startLocation = "8210 108 St NW Edmonton, AB T6E 5T2";
    String endLocation = "10189 106 Street Northwest, Edmonton, AB T5J 1H3";
    Number fare = 10.00;

    @Test
    public void user_auth_and_load() throws Exception{


        boolean authenticated = UserController.auth(userName,passWord);
        if(authenticated){
            user = UserController.loadUser(userName);
            assertEquals("billy",user.getName());
        }else{
            fail("User not Authenticated");
        }

    }

    @Test
    public void create_user_request() throws Exception{

        user.createRequest(startLocation,endLocation,fare); // Time request was created returned
        assertEquals(1,user.numberOfActiveRequests());
    }

    @Test
    public void check_request_start() throws Exception{
        UserRequest request = user.getLatestActiveRequest();
        assertEquals(startLocation, request.getStartLocation());
    }

    @Test
    public void check_request_end() throws Exception{
        UserRequest request = user.getLatestActiveRequest();
        assertEquals(endLocation, request.getEndLocation());
    }

    @Test
    public void check_request_fare()throws Exception{
        UserRequest request = user.getLatestActiveRequest();
        assertEquals(fare, request.getFare());
    }

}