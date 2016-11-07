package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by brianofrim on 2016-10-13.
 */
public class RequestController {

    public static ArrayList<Request> getRequestNear(String address, Number radius){
        return new ArrayList<Request>();
    }

    public static ArrayList<Request> getRequestNear(LatLng location, Number radius) {
        return new ArrayList<Request>();
    }

    // TEMPORARY POPULATION OF RANDOM REQUESTS!!!
    public static ArrayList<Request> tempFakeRequestList() {
        ArrayList<Request> returnValue = new ArrayList<Request>();
        returnValue.add(new Request(new LatLng(53.5443890, -113.4909270),
                new LatLng(54.07777, -113.50192), new User("test", "test@email.com")));
        returnValue.add(new Request(new LatLng(54.07777, -113.50192),
                new LatLng(53.5443890, -113.4909270), new User("test", "test@email.com")));
        returnValue.add(new Request(new LatLng(54.07777, -113.50192),
                new LatLng(53.5443890, -113.4909270), new User("test2", "test2@email.com")));
        return returnValue;
    }
}
