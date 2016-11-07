package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Controllers user and user functions
 * static so it can be shared by all activities
 */
public class UserController {
    private static User user = null;
    private static Mode mode;


    private static String USRFILE = "user.sav";

    public UserController(User u) {
        user = u;
    }


    static public void runBackgroundTasks(String usr, Activity activity, Boolean saveAfter) {
        ElasticsearchRequestController.GetPastRiderRequests riderTask = new ElasticsearchRequestController.GetPastRiderRequests();
        ElasticsearchRequestController.GetPastDriverRequests driverTask = new ElasticsearchRequestController.GetPastDriverRequests();
        riderTask.execute(usr);
        driverTask.execute(usr);
        try {
            user.setRequestList(riderTask.get());
            user.setAcceptedRequestList(riderTask.get());
        } catch (Exception e) {
            Log.i("Error", "AsyncTask failed to execute");
            e.printStackTrace();
        }

        // Saves user file after completion of asyncTasks if necessary
        if (saveAfter) {
            saveInFile(activity);
        }
    }


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
                user = checkUserTask.execute("Jane Doe").get();// force syncronous
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

    /**
     * Method to save the static user variable
     *
     * Stores it in internal storage as JSON in user.sav file
     */
    public static void saveInFile(Activity activity) {
         try {
             // Try to convert user to JSON and save it
             FileOutputStream fos = activity.openFileOutput(USRFILE, 0);
             OutputStreamWriter writer = new OutputStreamWriter(fos);
             Gson gson = new Gson();
             gson.toJson(user, writer);
             writer.flush();
         } catch (Exception e) {
             Log.i("Error", "Couldn't save file");
             throw new RuntimeException();
         }
    }

    public static void goOnline() {
    }

    public static void updateRequestList() {
    }
}
