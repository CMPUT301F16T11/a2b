package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static Mode mode = Mode.RIDER;
    private static LatLng lastLocation;


    private static String USRFILE = "user.sav";

    public UserController(User u) {
        user = u;
    }

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
            return user;
    }

    static public void updateUserInDb(){
        ElasticsearchUserController.UpdateUserInfoTask updateUserInfoTask = new ElasticsearchUserController.UpdateUserInfoTask();
        updateUserInfoTask.execute(UserController.getUser());
    }

    static public void updateLocation(LatLng location) {
        lastLocation = location;
    }
    static public LatLng getLastLocation() {
        return lastLocation;
    }



    // getters
   static public String getName() {
        return user.getName();
    }

   static public String getEmail() {
        return user.getEmail();
    }

    static public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    //setters

    static public void setName(String name) {
        user.setName(name);
    }

    static public void setEmail(String email) {
        user.setEmail(email);
    }

    static public void setPhoneNumber(String phoneNumber) {
         user.setPhoneNumber(phoneNumber);
    }


    // access elastic
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

    public static Boolean loadFromFile(Activity activity) {
        try {
            FileInputStream fis = activity.openFileInput(USRFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            user = new Gson().fromJson(in, User.class);
        } catch (FileNotFoundException f) {
            Log.i("File", "No saved user");
            return false;
        }

        return true;
    }

    public static void logOut(Context context) {
        context.deleteFile(USRFILE);

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
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
