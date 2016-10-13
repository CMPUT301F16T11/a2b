package com.cmput301f16t11.a2b;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by brianofrim on 2016-10-10.
 */
public class User {
    private ArrayList<UserRequest> requests;

    User(){
        requests = new ArrayList<UserRequest>();
    }

    public String getName(){
        return "";
    }

    public void createRequest(String start, String end, Number fare){
        requests.add(new UserRequest(start,end,fare));
    }

    public int numberOfActiveRequests(){
        return requests.size();
    }

    public UserRequest getLatestActiveRequest(){
        return requests.get(requests.size() - 1);
    }

    public ArrayList<UserRequest> getRequests(){
        return requests;
    }

    public void notifyUser(UserRequest r) {
    }
}
