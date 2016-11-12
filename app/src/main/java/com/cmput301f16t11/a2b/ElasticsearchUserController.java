package com.cmput301f16t11.a2b;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Elasticsearch controller to deal with user stuff on the elasticsearch server
 */

public class ElasticsearchUserController {
    private static JestDroidClient client;
    private static String index = "f16t11";
    private static String userType = "user";

    /**
     * AsyncTask used to determine whether or not the entered username is already taken.
     */
    public static class CheckUserTask extends AsyncTask<String, Void, User> {
        /**
         * Determines whether or not the username is taken
         *
         * @param params the username in question
         * @return User if username is available. null otherwise
         */
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


                    User user = result.getSourceAsObject(User.class);

//                    // ensure that the users id is set.
//                    // this is for debugging purposes and can be removed in production
//                    if (user == null) {
//                        // parse the response for the id
//                        JsonObject jsonResponse = result.getJsonObject();
//                        JsonObject hits = jsonResponse.getAsJsonObject("hits");
//                        JsonArray actualHits = hits.getAsJsonArray("hits");
//                        JsonObject firstHit = actualHits.get(0).getAsJsonObject();
//                        String id = firstHit.get("_id").toString();
//                        user.setId(id);
//                    }

                    if (user!=null && user.getName().equals(params[0])) {
                        return user;
                    }
                    else {
                        return new User();
                    }
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
     * Given a user id, generate the user object
     */
    public static class RetrieveUserInfo extends AsyncTask<User, Void, User> {
        /**
         * Given a user id, generate the user object
         * @param users userID
         * @return user obj
         */
        @Override
        protected User doInBackground(User... users) {

            verifySettings();

            Get get = new Get.Builder(index, users[0].getId()).type("user").build();

            User user = new User();

            try {
                JestResult result = client.execute(get);
                if (result.isSucceeded()) {
                    user = result.getSourceAsObject(User.class);
                } else {
                    Log.i("Error", "Filed to find request");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return user;
        }
    }

    /**
     * AsyncTask used to add the user to the elasticsearch server
     *
     * Input: User object
     * Output: Boolean representing elasticsearch result
     */
    public static class AddUserTask extends AsyncTask<User, Void, Boolean> {
        /**
         * Adds a user to elasticsearch server
         *
         * @param users the user to add
         * @return true if successful, false otherwise
         */
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
        /**
         * Updates a user account's details
         *
         * @param users new user obj
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(User... users) {
            verifySettings();

            // Pretty much the same as AddUserTask but an existing id is specified so the entry
            // gets overwritten

            Index userIndex = new Index.Builder(users[0]).index(index).type(userType).id(users[0].getId()).build();

            try {
                DocumentResult result = client.execute(userIndex);
                if (!result.isSucceeded()) {
                    Log.i("Error", "Elasticsearch failed to update user");
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
            //DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
