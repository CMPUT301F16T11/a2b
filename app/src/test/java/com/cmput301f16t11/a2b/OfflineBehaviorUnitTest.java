package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static junit.framework.Assert.*;



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

public class OfflineBehaviorUnitTest{
    private LatLng start = new LatLng(53.5443890,-113.4909270);
    private LatLng end = new LatLng(53.5231674,-113.5256026);
    private int fare = 10;
    private User user = new User();
    ArrayList<UserRequest> requestList;
    UserRequest request;
    UserRequest request1;
    UserRequest request2;


    @Before
    public void setup(){
        requestList = new ArrayList<>();
        //build a list of requests
        request = new UserRequest(start,end,fare+1,user.getId());
        request1 = new UserRequest(start,end,fare+2,user.getId());
        request2 = new UserRequest(start,end,fare+3,user.getId());
        request.setId("Hello123");
        request1.setId("Hello321");
        request2.setId("Bye123");
        //add the requests to the list
        requestList.add(request);
        requestList.add(request1);
        requestList.add(request2);
        CommandStack.clearCommands();
    }
    /**
     * US 08.01.01
     * As an driver, I want to see requests that I already accepted while offline.
     * US 08.02.01
     * As a rider, I want to see requests that I have made while offline.
     */
    @Test
    public void testOfflineAcceptedRequest(){

        String mockfile;
        Gson gson = new Gson();
        mockfile = gson.toJson(requestList);
        // test that serialization has worked, if yes it means that the list can be written to a file
        Type listType = new TypeToken<ArrayList<UserRequest>>() {
        }.getType();
        ArrayList<UserRequest> newRequestList = gson.fromJson(mockfile, listType);

        assertTrue(newRequestList.get(0).getId().equals(requestList.get(0).getId()));
        assertTrue(newRequestList.get(1).getId().equals(requestList.get(1).getId()));
        assertTrue(newRequestList.size() == 3);


    }


    /**
     * US 08.03.01
     * As a rider, I want to make requests that will be sent once I get connectivity again.
     * US 08.04.01
     * As a driver, I want to accept requests that will be sent once I get connectivity again.
     */
    @Test
    public void testSendMadeRequestsAfterConnection(){
        UserRequest request = new UserRequest(start,end,fare,user.getId());
        UserRequest request2 = new UserRequest(new LatLng(12,34), new LatLng(32,12), 123, user.getId());
        request.setId("kf123");
        request2.setId("kf432");

        ArrayList<UserRequest> stack = new ArrayList<UserRequest>();
        stack.add(request);
        stack.add(request2);

        String mockfile;
        Gson gson = new Gson();
        mockfile = gson.toJson(stack);
        // test that serialization has worked, if yes it means that the list can be written to a file
        Type listType = new TypeToken<ArrayList<UserRequest>>() {
        }.getType();

        ArrayList<UserRequest> newRequestList = gson.fromJson(mockfile, listType);
        // simulate a stack
        assertTrue(newRequestList.get(newRequestList.size()-1).getId().equals(request2.getId()));
        assertTrue(newRequestList.get(newRequestList.size()-2).getId().equals(request.getId()));


    }
}
