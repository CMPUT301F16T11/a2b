package com.cmput301f16t11.a2b;

/**
 * Created by tothd on 10/12/2016.
 * US 08.01.01
 As an driver, I want to see requests that I already accepted while offline.

 US 08.02.01
 As a rider, I want to see requests that I have made while offline.

 US 08.03.01
 As a rider, I want to make requests that will be sent once I get connectivity again.


 US 08.04.01
 As a driver, I want to accept requests that will be sent once I get connectivity again.
 *
 */

public class OfflineBehaviorUnitTest {

    String username = "username";

    User driver = UserController.loadUser(username);
    String start = "here";
    String end = "there";
    int fare = 10;

    UserRequest request = new UserRequest(start,end,fare);


    public void testOfflineAcceptedRequest(){
        //build a list of requests
        //set them to accepted
        //save them to a file
        //set connection to offline

        //grab saved list of requests from some file???

        //check and see if this list same as given one using test case
    }

    public void testOfflineMadeRequests(){
        //build a list of requests
        //save them to a file
        //go offline
        //grab saved ist of requests
        //check that saved list is the same list before going offline
    }

    public void testSendMadeRequestsAfterConnection(){
        //go offline
        //create a request
        //go online
        //check if online list has your created request

    }

    public void testAcceptRequestsAfterConnection(){
        //go offline
        //grab list of requests
        //accept one of them
        //go online
        //check that accepted request has been sent 
    }
}
