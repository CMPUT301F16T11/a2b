package com.cmput301f16t11.a2b;

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

   static public String getNewUserName() {
        return "Daniel";
    }
   static public String getNewPass() {
        return "OilersSuck";
    }
   static public String getEmail() {
        return "mcjesus@ualberta.ca";
    }

}
