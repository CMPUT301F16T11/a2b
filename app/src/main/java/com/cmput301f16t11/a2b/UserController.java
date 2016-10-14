package com.cmput301f16t11.a2b;

import java.util.ArrayList;

/**
 * Created by brianofrim on 2016-10-10.
 */
public class UserController {
    static public boolean auth(String userName, String password){
        return true;
    }
    static public User loadUser(String userName){
        return new User();
    }

   static public String getNewUserName() {
        return "Daniel";
    }
   static public String getNewPass() {
        return "Flamers and Oilers Suck";
    }
   static public String getEmail() {
        return "mcjesus@ualberta.ca";
    }

    public static ArrayList<UserRequest> getRequestList() {

        ArrayList<UserRequest> fakeList = new ArrayList<>();
        fakeList.add(new UserRequest("start", "end", 0));
        return fakeList;
    }

    public static void setOffline() {
    }

    public static void saveInFile(ArrayList<UserRequest> requestList) {
    }

    public static void goOnline() {
    }

    public static void updateRequestList() {
    }
}
