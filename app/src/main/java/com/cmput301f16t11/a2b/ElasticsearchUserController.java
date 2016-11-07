package com.cmput301f16t11.a2b;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.searchbox.client.JestResult;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * Created by Wilky on 11/5/2016.
 */

public class ElasticsearchUserController {
    private static JestDroidClient client;
    private static String index = "f16t11";
    private static String userType = "user";

    /**
     * AsyncTask used to determine whether or not the entered username is already taken.
     *
     * Input: Username string
     * Output: User object if exists, null if no user exists
     */
    public static class CheckUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {

            verifySettings();

            if (params[0]=="" | params[0]==null) {
                return new User();
            }

            String search_string = "{\"query\": {\"match\": {\"userName\": \"" + params[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(userType)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    return result.getSourceAsObject(User.class);
                } else {
                    return new User();
                }
            } catch (IOException e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return new User();
            }
        }
    }

    /**
     * AsyncTask used to add the user to the elasticsearch server
     *
     * Input: User object
     * Output: Boolean representing elasticsearch result
     */
    public static class AddUserTask extends AsyncTask<User, Void, Boolean> {
        @Override
        protected Boolean doInBackground(User... users) {
            verifySettings();

            Index userIndex = new Index.Builder(users[0]).index(index).type(userType).build();

            try {
                DocumentResult result = client.execute(userIndex);
                if (result.isSucceeded()) {
                    users[0].setId(result.getId());
                } else {
                    Log.i("Error", "Elasticsearch failed to add user");
                    return false;
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to add user to elasticsearch");
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }


    /**
     * AsyncTask used to overwrite an existing user in the database
     *
     * Input: User object
     * Output: Boolean representing elasticsearch result
     */

    public static class UpdateUserInfoTask extends AsyncTask<User, Void, Boolean> {
        @Override
        protected Boolean doInBackground(User... users) {
            verifySettings();

            // Pretty much the same as AddUserTask but an existing id is specified so the entry
            // gets overwritten

            Index userIndex = new Index.Builder(users[0]).index(index).type(userType).id(users[0].getId()).build();

            try {
                DocumentResult result = client.execute(userIndex);
                if (!result.isSucceeded())  {
                    Log.i("Error", "Elasticsearch failed to add user");
                    return false;
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to add user to elasticsearch");
                e.printStackTrace();
                return false;
            }

            return true;
        }


    }


    private static void verifySettings() {
        // Initialize client if necessary
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://35.162.68.100:9200");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
