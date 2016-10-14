package com.cmput301f16t11.a2b;

import java.util.ArrayList;

/**
 * Created by brianofrim on 2016-10-10.
 */
public class User {

    private ArrayList<UserRequest> requests;
    private ArrayList<UserRequest> acceptedRequests;
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;

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

   //Getters
    public String getName(){
        return userName;
    }
    public String getPassWord() { return passWord;}
    public String getEmail() { return email;}
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public ArrayList<UserRequest> getRequests(){
        return requests;
    }
    public ArrayList<UserRequest> getAcceptedRequests() {
        return acceptedRequests;
    }
    public UserRequest getLatestActiveRequest(){return requests.get(requests.size() - 1);}

    //Setters
    public void setRequestList(ArrayList<UserRequest> requestList){
        requests = requestList;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setName(String name) {this.userName = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPassWord(String pass) {this.passWord = pass;}

    // Request transactions
    public void createRequest(String start, String end, Number fare){
        requests.add(new UserRequest(start,end,fare));
    }
    public void addRequest(UserRequest request){

        requests.add(request);
    }
    public void addAcceptedRequest(UserRequest request) {

        acceptedRequests.add(request);
    }
    public void removeRequest(UserRequest request) {

        requests.remove(request);
    }

    public int numberOfActiveRequests(){

        return requests.size();
    }
    public void notifyUser(UserRequest r) {
    }

    public boolean hasAcceptedRequests(UserRequest request) {
        return true;
    }


}
