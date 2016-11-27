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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * The type File controller. Handles Saving and loading of files
 * for offline use.
 *
 * This work contains a derivative of a stackoverflow answer to "Files from res file to sdcard on
 * android," by "DMcP89," a stackoverflow user.
 * It is used under CC-BY-SA 2.0 by CMPUT301F16T11.
 * Available here: http://stackoverflow.com/questions/5943916/files-from-res-file-to-sdcard-on-android
 * Date accessed: Nov. 25, 2016
 *
 * This work contains code from a stackoverflow answer to
 * "Detect whether there is an Internet connection available on Android [duplicate]," by
 * "dan," edited by, "Palec," stackoverflow users.
 * It is used under CC-BY-SA 2.0 by CMPUT301F16T11.
 * Available here: http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
 * Date accessed: Nov. 26, 2016
 */
public class FileController {


    private static Context context;

    /**
     * Set context.
     *
     * @param con the con
     */
    public static void setContext(Context con){
        context = con;
    }



    /**
     * Load from file array list.
     *
     * @param FILENAME the filename that is to be loaded from
     * @return the array list of requests
     */
    public static ArrayList<UserRequest> loadFromFile(String FILENAME) {
        ArrayList<UserRequest> requests;


        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<UserRequest>>() {
            }.getType();

            requests = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            requests = new ArrayList<UserRequest>();
        }
        return requests;
    }


    /**
     * Writes mbtiles file from res to android storage for offline maps usage
     *
     * @param context applicatiton context
     * @return String of direct path to map file in android memory
     */
    public static String writeMapFile(Context context) {
        InputStream ins = context.getResources().openRawResource (R.raw.map);
        String filename = "";
        try {
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();
            filename = context.getFilesDir().toString() + File.separator + "map";
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            Log.e("FM", e.toString());
        }
        return filename;
    }

    /**
     * Load from file user user.
     *
     * @param USRFILE the usrfile
     * @return the user
     */
    public static User loadFromFileUser(String USRFILE) {
        User user;
        try {
            FileInputStream fis = context.openFileInput(USRFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            user = new Gson().fromJson(in, User.class);
        } catch (FileNotFoundException f) {
            Log.i("File", "No saved user");
            return null;
        }
        return user;

        }


    /**
     * Save in file.
     *
     * @param offlineRequestListIn the offline request list in
     * @param FILENAME             the filename
     */
    public static void saveInFile(ArrayList<UserRequest> offlineRequestListIn, String FILENAME) {

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
        context.deleteFile("acceptedByMeNames.sav"); // delete file
        context.deleteFile("acceptedByMe.sav");
        context.deleteFile("riderOwnerRequests.sav");
        context.deleteFile("riderOwnerRequestsNames.sav");
        context.deleteFile("user.sav");
        context.deleteFile("nearbyNames.sav");
    }


    /**
     * Is network available boolean.
     * checks if there is a network connection
     *
     * @return the boolean
     */

    public static boolean isNetworkAvailable(Context passedInContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) passedInContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}