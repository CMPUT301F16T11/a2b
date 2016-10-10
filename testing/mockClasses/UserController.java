package com.example.brianofrim.a2btests;

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

}
