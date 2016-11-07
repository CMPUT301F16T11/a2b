package com.cmput301f16t11.a2b;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;

/**
 * Controllers user and user functions
 * static so it can be shared by all activities
 */
public class UserController {
    private static User user = null;
    private static Mode mode;

//    public UserController(String username) {
//        // TODO: sign in.
//        // temp for testing:
//        user = new User("TEST", "TEST@email.com");
//    }



//    static public boolean auth(String username){
//        // no idea what this is... - joey
//        // is this to statically check auth?
//        return true;
//    }



    static public void setUser(User newUser) {
        user = newUser;
    }

    static public void setMode(Mode newMode) {
        mode = newMode;
    }

    static public Mode checkMode() {
        return mode;
    }

    static public User getUser() {
        if(user != null){
            return user;
        }else{ // should always be set but for dev purposes return an existing object
            // TO DO: depricate this before production
            ElasticsearchUserController.CheckUserTask checkUserTask = new ElasticsearchUserController.CheckUserTask();
            try{
                user = checkUserTask.execute("*").get();// force syncronous
            }catch (Exception e){
                user = new User();
            }

            return user;
        }
    }

    // push user changes to the data base
    static public void updateUserInDb(){
        ElasticsearchUserController.UpdateUserInfoTask updateUserInfoTask = new ElasticsearchUserController.UpdateUserInfoTask();
        updateUserInfoTask.execute(user);
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
