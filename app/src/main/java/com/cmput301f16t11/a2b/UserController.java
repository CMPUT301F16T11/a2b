package com.cmput301f16t11.a2b;

import java.util.ArrayList;

/**
 * Controllers user and user functions
 *
 */
public class UserController {
    private User user;
    private Mode mode;

    public UserController(String username) {
        // TODO: sign in.
        // temp for testing:
        user = new User("TEST", "TEST@email.com");
    }

    static public boolean auth(String username){
        // no idea what this is... - joey
        // is this to statically check auth?
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode checkMode() {
        return this.mode;
    }






    // time to depreciate this???
    static public User loadUser(String username){
        return new User();
    }

   static public String getNewUserName() {
        return "Daniel";
    }
   static public String getNewPass() {
        return "test";
    }
   static public String getEmail() {
        return "test@ualberta.ca";
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
