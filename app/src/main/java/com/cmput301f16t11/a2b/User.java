package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * Model for User class
 */
public class User {
    @JestId
    private String id;
    private transient ArrayList<UserRequest> requests;
    private transient ArrayList<UserRequest> acceptedRequests;
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;

    User(){
        requests = new ArrayList<UserRequest>();
        acceptedRequests = new ArrayList<UserRequest>();
    }

    User(String name, String email) {
        requests = new ArrayList<UserRequest>();
        acceptedRequests = new ArrayList<UserRequest>();
        userName = name;
        this.email = email;
    }

    User(String name,  String email, String phone) {
        requests = new ArrayList<UserRequest>();
        acceptedRequests = new ArrayList<UserRequest>();
        userName = name;
        this.email = email;
        phoneNumber = phone;
    }


    User(String name, String pass, String email, String phone) {
        requests = new ArrayList<UserRequest>();
        acceptedRequests = new ArrayList<UserRequest>();
        userName = name;
        passWord = pass;
        this.email = email;
        phoneNumber = phone;
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
    public String getId() {return id;}
    public String toString() {
        return this.userName;
    }

    //Setters
    public void setAcceptedRequestList(ArrayList<UserRequest> requestList) { acceptedRequests = requestList;}
    public void setRequestList(ArrayList<UserRequest> requestList){
        requests = requestList;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setName(String name) {this.userName = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPassWord(String pass) {this.passWord = pass;}
    public void setId(String id) {this.id = id;}

    // Request transactions
    public void createRequest(LatLng start, LatLng end, Number fare){
        requests.add(new UserRequest(start,end,fare, this.userName));
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
