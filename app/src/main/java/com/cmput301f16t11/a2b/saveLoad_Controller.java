package com.cmput301f16t11.a2b;

/**
 * Created by kelvinliang on 2016-11-23.
 */

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.provider.Telephony.Mms.Part.FILENAME;


// code from LonelyTwitter
public class saveLoad_Controller {
    private Context context;
    private ArrayList<UserRequest> offlineRequestList;
    //private static final String FILENAME = "file.sav";

    public saveLoad_Controller(Context context) {
        this.context = context;
    }

    public ArrayList<UserRequest> loadFromFile(String FILENAME) {

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<ArrayList<UserRequest>>() {
            }.getType();

            offlineRequestList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            offlineRequestList = new ArrayList<UserRequest>();
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return offlineRequestList;
    }

    public void saveInFile(ArrayList<UserRequest> offlineRequestListIn, String FILENAME) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(offlineRequestListIn, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void clear() {
        offlineRequestList.clear();
        context.deleteFile(FILENAME); // delete file
    }
}