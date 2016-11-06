package com.cmput301f16t11.a2b;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by brianofrim on 2016-11-06.
 */
public class RESTAPI {
    private static JestDroidClient client;
    private static String index = "f16t11";
    private static String userType = "user";
    private static String activeRequests = "activeRequests";
    private static String pastRequests = "pastRequests";


    public static class addUser extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            verifySettings();

            // TODO: Edit this string so that it properly searches for users with matching usernames
            String search_string = "{\"query\": {\"match\": {\"userName\": \"" + params[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(userType)
                    .build();

            try {
                SearchResult result = client.execute(search);
                result.
                if (result.isSucceeded()) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                return true;
            }
        }
    }


    public static class addUser extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String userName, String phoneNumber, String email) {

            verifySettings();

            // TODO: Edit this string so that it properly searches for users with matching usernames
            String search_string = "{\"query\": {\"match\": {\"userName\": \"" + params[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(userType)
                    .build();

            try {
                SearchResult result = client.execute(search);
                result.
                if (result.isSucceeded()) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                return true;
            }
        }
    }



    private static void verifySettings() {
        // Initialize client if necessary
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
