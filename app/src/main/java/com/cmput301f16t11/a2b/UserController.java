package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;

import static com.cmput301f16t11.a2b.R.id.user;

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
        mode = Mode.RIDER;
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

    static public void updateUserInDb(){
        ElasticsearchUserController.UpdateUserInfoTask updateUserInfoTask = new ElasticsearchUserController.UpdateUserInfoTask();
        updateUserInfoTask.execute(UserController.getUser());
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
        return fakeList;
    }

    public static void setOffline() {
    }


    /**
     * Method to save the static user variable
     *
     * Stores it in internal storage as JSON in user.sav file\
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

    public static void runBackgroundTasks(String name, LoginActivity loginActivity, boolean b) {
    }

    public static void setClosedRequestsAsRider(Collection<UserRequest> requests) {
        user.setClosedRequestsAsRider(requests);
    }
    public static void setClosedRequestsAsDriver(Collection<UserRequest> requests) {
        user.setClosedRequestsAsDriver(requests);
    }
    public static void setActiveRequestsAsRider(Collection<UserRequest> requests) {
        user.setActiveRequestsAsRider(requests);

    }
    public static void setActiveRequestAsDriver(Collection<UserRequest> requests) {
        user.setActiveRequestsAsDriver(requests);
    }
}
