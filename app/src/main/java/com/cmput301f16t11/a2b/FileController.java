package com.cmput301f16t11.a2b;

/**
 * Created by kelvinliang on 2016-11-23.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * The type File controller. Handles Saving and loading of files
 * for offline use.
 */
public class FileController {

<<<<<<< HEAD

=======
>>>>>>> 6a70fac3924707fcaf105bcec8c96a2a8e1904c5
    private static Context context;

    /**
     * Set context.
     *
     * @param con the con
     */
    public static void setContext(Context con){
        context = con;
    }


    public static ArrayList<Command> loadFromFile(String FILENAME) {
        ArrayList<Command> requests;
=======
    /**
     * Load from file array list.
     *
     * @param FILENAME the filename that is to be loaded from
     * @return the array list of requests
     */
    public static ArrayList<UserRequest> loadFromFile(String FILENAME) {
        ArrayList<UserRequest> requests;
>>>>>>> 6a70fac3924707fcaf105bcec8c96a2a8e1904c5

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<ArrayList<Command>>() {
            }.getType();

            requests = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            requests = new ArrayList<Command>();
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return requests;
    }

    /**
     * Load from file map hash map.
     *
     * @param FILENAME the filename
     * @return the hash map
     */
    public static HashMap<String, String> loadFromFileMap(String FILENAME) {
        HashMap<String, String> dic;
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<HashMap<String, String>>() {
            }.getType();

            dic = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            dic = new HashMap<String, String>();
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return dic;
    }

    /**
     * Load from file user user.
     *
     * @param USRFILE the usrfile
     * @return the user
     */
    public static User loadFromFileUser(String USRFILE) {
        User user = new User();
        try {
            FileInputStream fis = context.openFileInput(USRFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            user = new Gson().fromJson(in, User.class);
        } catch (FileNotFoundException f) {
            Log.i("File", "No saved user");
        }

        return user;

        }

<<<<<<< HEAD
    public static void saveInFile(ArrayList<Command> offlineRequestListIn, String FILENAME) {
=======
    /**
     * Save in file.
     *
     * @param offlineRequestListIn the offline request list in
     * @param FILENAME             the filename
     */
    public static void saveInFile(ArrayList<UserRequest> offlineRequestListIn, String FILENAME) {
>>>>>>> 6a70fac3924707fcaf105bcec8c96a2a8e1904c5
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            Gson gson = new Gson();
            gson.toJson(offlineRequestListIn, writer);
            writer.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        storeUserNames(offlineRequestListIn);
    }

    /**
     * Save in file map.
     *
     * @param map the map
     */
    public static void saveInFileMap(HashMap<String, String> map) {
        try {
            FileOutputStream fos = context.openFileOutput("names.sav", 0);
            //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            Gson gson = new Gson();
            gson.toJson(map, writer);
            writer.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Save in file user.
     *
     * @param user     the user
     * @param FILENAME the filename
     */
    public static void saveInFileUser(User user, String FILENAME) {
        try {
            // Try to convert user to JSON and save it
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(user, writer);
            writer.flush();
        } catch (Exception e) {
            Log.i("Error", "Couldn't save file");
            throw new RuntimeException();
        }
    }

    /**
     * Clear.
     */
    public static void clear() {
        context.deleteFile("names.sav"); // delete file
        context.deleteFile("acceptedByMe.sav");
        context.deleteFile("riderOwnerRequests.sav");
    }

<<<<<<< HEAD
    public static void storeUserNames(ArrayList<Command> requests) {
=======
    /**
     * Store user names.
     *
     * @param requests the requests
     */
    public static void storeUserNames(ArrayList<UserRequest> requests) {
>>>>>>> 6a70fac3924707fcaf105bcec8c96a2a8e1904c5

        HashMap<String, String> names = new HashMap<String, String>();
        loadFromFileMap("names.sav");
        for (Command command : requests) {
            UserRequest request = command.getRequest();
            String id = request.getRiderID();
            String userName = UserController.getUserFromId(id).getName();
            names.put(id, userName);
        }
        saveInFileMap(names);
    }

    /**
     * Is network available boolean.
     * checks if there is a network connection
     *
     * @param c the c
     * @return the boolean
     */
// http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}