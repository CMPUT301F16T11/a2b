package com.cmput301f16t11.a2b;

import java.util.ArrayList;

/**
 * Created by brianofrim on 2016-10-10.
 */
public class UserController {
    static public boolean auth(String un, String pw){
        return true;
    }
    static public User loadUser(String un){
        return new User();
    }
    static public Driver loadDriver(String un,int r) {return new Driver(un,r);}

    public static ArrayList<UserRequest> getRequestList() {
        return null;
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
