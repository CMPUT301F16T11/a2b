package com.cmput301f16t11.a2b;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by brianofrim on 2016-10-10.
 */
public class User {

    private ArrayList<UserRequest> requests;
    private ArrayList<UserRequest> acceptedRequests;
    String userName;
    String passWord;
    String email;

    User(){
        requests = new ArrayList<UserRequest>();
        acceptedRequests = new ArrayList<UserRequest>();
        userName = "n/a";
        passWord = "n/a";
        email = "n/a";
    }

    User(String name, String pass, String email) {
        requests = new ArrayList<UserRequest>();
        acceptedRequests = new ArrayList<UserRequest>();
        userName = name;
        passWord = email;
        email = pass;
    }

    public String getName(){
        return userName;
    }

    public String getPassWord() { return passWord;}

    public String getEmail() { return email;}

    public void setName(String name) {this.userName = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPassWord(String pass) {this.passWord = pass;}

    public void createRequest(String start, String end, Number fare){
        requests.add(new UserRequest(start,end,fare));
    }

    public void removeRequest(UserRequest request) {
        requests.remove(request);
    }

    public int numberOfActiveRequests(){
        return requests.size();
    }

    public UserRequest getLatestActiveRequest(){
        return requests.get(requests.size() - 1);
    }

    public ArrayList<UserRequest> getRequests(String location){
        return requests;
    }

    public ArrayList<UserRequest> getAllRequests() {
        return requests;
    }
    public void addAcceptedRequest(UserRequest request) {
        acceptedRequests.add(request);
    }
    public boolean hasAcceptedRequests() {
        return true;
    }
}
