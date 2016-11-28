package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Controllers user and user functions
 * static so it can be shared by all activities
 *
 * SINGLETON DESIGN PATTERN
 * STATE DESIGN PATTERN (mode)
 * STATE DESIGN PATTERN (user)
 */
public class UserController {
    private static User user = null;
    private static Mode mode = Mode.RIDER;



    private static String USRFILE = "user.sav";

    /**
     * Sets the user for the current session
     *
     * @param newUser the user obj for curr session
     */
    static public void setUser(User newUser) {
        user = newUser;
    }

    /**
     * sets the mode for the current session
     *
     * @see Mode
     * @param newMode the mode to set
     */
    static public void setMode(Mode newMode) {
        mode = newMode;
    }

    /**
     * returns the current mode
     *
     * @see Mode
     *
     * @return current mode
     */
    static public Mode checkMode() {
        return mode;
    }

    static public Boolean canDrive() {
        return user.canDrive();
    }

    static public void updateRating(int r, String driverName) {
        User user = UserController.getUserFromName(driverName);
        int currTotalRating = user.getTotalRating();
        int currTotal = user.getNumRatings();

        int newTotal = currTotal+1;
        int newTotalRating = currTotalRating + r;
        DecimalFormat format = new DecimalFormat("#.00");
        double newRating = Double.parseDouble(format.format(((double)newTotalRating)/newTotal));

        user.setRating(newRating);
        user.setNumRatings(newTotal);
        user.setTotalRating(newTotalRating);

        ElasticsearchUserController.AddToDriverRating updateRatingTask = new ElasticsearchUserController.AddToDriverRating();
        updateRatingTask.execute(user.getId(), String.valueOf(r), String.valueOf(newRating));
    }

    /**
     * gets the user for the current session
     *
     * @see User
     * @return the current user obj
     */
    static public User getUser() {
            return user;
    }

    /**
     * Updates a user value in elasticsearchserver
     *
     * @see ElasticsearchUserController
     */
    static public void updateUserInDb(){
        ElasticsearchUserController.UpdateUserInfoTask updateUserInfoTask = new ElasticsearchUserController.UpdateUserInfoTask();
        updateUserInfoTask.execute(UserController.getUser());
    }

    // getters

    /**
     * Gets username for curr user session
     *
     * @return String username
     */
   static public String getName() {
        return user.getName();
    }

    /**
     * Gets email for curr user session
     *
     * @return String email
     */
   static public String getEmail() {
        return user.getEmail();
    }

    /**
     * Gets phone number for curr user session
     *
     * @return String phone number
     */
    static public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    //setters

    /**
     * Sets the current username
     *
     * @param name the curr username
     */
    static public void setName(String name) {
        user.setName(name);
    }

    /**
     * Sets the current email
     *
     * @param email the curr email
     */
    static public void setEmail(String email) {
        user.setEmail(email);
    }

    /**
     * Sets the curr phone number
     *
     * @param phoneNumber the curr phone number
     */
    static public void setPhoneNumber(String phoneNumber) {
         user.setPhoneNumber(phoneNumber);
    }


    /**
     * Method to save the static user variable
     *
     * Stores it in internal storage as JSON in user.sav file\
     */
    public static void saveInFile(Activity activity) {
        FileController.saveInFileUser(user, USRFILE, activity);
    }

    /**
     * Loads user data from file if still logged in
     *
     * @param activity curr applciation context
     * @return true if successful, false if no saved user
     */
    public static Boolean loadFromFile(Activity activity) {
        user = FileController.loadFromFileUser(USRFILE, activity);
        if(user != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * logs the user out of their current session
     *
     * @param context current application context
     */
    public static void logOut(Context context) {

        UserController.setUser(null);
        UserController.setMode(Mode.RIDER);

        //delete all offline files
        FileController.clear(context);

        //Stop all the services if they are running
        RiderNotificationService.stopRiderService();
        DriverNotificationService.stopDriverService();

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * Gets user object from user name by querying elasticsearch server
     *
     * @see ElasticsearchUserController
     * @See User
     *
     * @param username username of user
     * @return the User obj corresponding to username
     */
    public static User getUserFromName(String username) {
        User user = new User();
        try {
            ElasticsearchUserController.CheckUserTask searchController =
                    new ElasticsearchUserController.CheckUserTask();
            searchController.execute(username);
            user = searchController.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Calls the elastic search to get the user object
     * @param id id of the user you want to get the user object from
     * @return a user object of the given id
     */
    public static User getUserFromId(String id){
        User user = new User();
        try {
            ElasticsearchUserController.getUsersFromId userImpl =
                    new ElasticsearchUserController.getUsersFromId();
            userImpl.execute(id);
            user = userImpl.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static ArrayList<User> getUsersFromIds(ArrayList<String> userIds){

        ArrayList<User> users = new ArrayList<>();
        try {
            ElasticsearchUserController.getUsersFromIds userImpl =
                    new ElasticsearchUserController.getUsersFromIds();
            userImpl.execute(userIds);
            users = userImpl.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
}
