package com.cmput301f16t11.a2b;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.MultiGet;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * Elasticsearch controller to deal with user stuff on the elasticsearch server
 *
 * BUILDER PATTERN USED
 * Jests Builder classes are used to build commands executed by the client
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
    public static class RetrieveUserInfoFromId extends AsyncTask<String, Void, User> {
        /**
         * Given a user id, generate the user object
         * @param users userID
         * @return user obj
         */
        @Override
        protected User doInBackground(String... users) {

            verifySettings();

            Get get = new Get.Builder(index, users[0]).type("user").build();

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
     * Removes a user from the DB
     */
    public static class DeleteUserTask extends AsyncTask<User, Void, Boolean> {
        /**
         * Deletes a user from db
         *
         * @param users the user obj to delete
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(User... users) {
            // Delete the user
            try {
                DocumentResult result = client.execute(new Delete.Builder(users[0].getId())
                        .index(index)
                        .type(userType)
                        .build());
                if (result.isSucceeded()) {
                } else {
                    Log.i("Error", "Elasticsearch failed to delete user");
                    return false;
                }
            } catch (Exception e) {
                Log.i("Error", "Elasticsearch failed to delete user");
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

    /**
     * AsyncTask used to overwrite an existing user in the database
     *
     * Input: User object
     * Output: Boolean representing elasticsearch result
     */
    public static class UpdateUserInfoOfflineTask extends AsyncTask<User, Void, Boolean> {
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
            boolean waiting = true;
            while (waiting) {
                try {
                    DocumentResult result = client.execute(userIndex);
                    if (result.isSucceeded()) {
                        waiting = false;
                    } else {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            Log.i("Error", "Failed to sleep");
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    Log.i("Error", "Failed to add user to elasticsearch");
                    e.printStackTrace();
                    try {
                        Thread.sleep(3000);
                    } catch (Exception f) {
                        Log.i("Error", "Failed to sleep");
                        f.printStackTrace();
                    }
                }
            }
            return true;
        }
    }

    public static class getUsersFromIds extends AsyncTask<ArrayList<String>,Void,ArrayList<User>>{
        @Override
        protected ArrayList<User> doInBackground(ArrayList<String> ... userIds) {
            verifySettings();

            Collection<String> userIdList = userIds[0];
            ArrayList<User> users = new ArrayList<>();

            MultiGet multiGet = new MultiGet.Builder.ById(index,userType).addId(userIdList).build();

            try {
                JestResult result = client.execute(multiGet);
                if (result.isSucceeded()) {
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }else{
                    Log.i("Error","Filed to find request");
                    return users;
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return users;
            }
            return users;
        }
    }

    public static class getUsersFromId extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... userId) {
            verifySettings();

            Get get = new Get.Builder(index,userId[0]).type(userType).build();

            User user = null;

            try {
                JestResult result = client.execute(get);
                if (result.isSucceeded()) {
                    user = result.getSourceAsObject(User.class);
                }else{
                    Log.i("Error","Filed to find request");
                    return null;
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return null;
            }

            return user;
        }
    }

    public static class AddToDriverRating extends AsyncTask<String, Void, Boolean>{
        @Override
        /**
         * info[0] = the driver id
         * info[1] = the rating / 5 given by the user
         * info[2] = new average rating as double string
         */
        protected Boolean doInBackground(String... info) {
            verifySettings();

            // update scripts
            String updateNumRatings = "{\n" +
                    "    \"script\" : \"ctx._source.numRatings += one\",\n" +
                    "    \"params\" : {\n" +
                    "        \"one\" : 1\n" +
                    "    }\n" +
                    "}";
            String updateTotalRating = "{\n" +
                    "    \"script\" : \"ctx._source.totalRating += rating\",\n" +
                    "    \"params\" : {\n" +
                    "        \"rating\" : "+ info[1] +"\n" +
                    "    }\n" +
                    "}";
            String updateAvgRating = "{\n" +
                    "    \"script\" : \"ctx._source.rating = rating\",\n" +
                    "    \"params\" : {\n" +
                    "        \"rating\" : "+ info[2] +"\n" +
                    "    }\n" +
                    "}";


            //preform the update to number of ratings
            try {
                DocumentResult result = client.execute(new Update.Builder(updateNumRatings).index(index).type(userType).id(info[0]).build());

                if (!result.isSucceeded()) {
                    Log.i("Error", "Elasticsearch failed to update user rating");
                    return false;
                }

            } catch (Exception e) {
                Log.i("Error", "Failed to update user rating");
                e.printStackTrace();
                return false;
            }

            //preform the update to the total rating
            try {
                DocumentResult result = client.execute(new Update.Builder(updateTotalRating).index(index).type(userType).id(info[0]).build());

                if (!result.isSucceeded()) {
                    Log.i("Error", "Elasticsearch failed to update user rating");
                    return false;
                }

            } catch (Exception e) {
                Log.i("Error", "Failed to update user rating");
                e.printStackTrace();
                return false;
            }

            //preform the update to the total rating
            try {
                DocumentResult result = client.execute(new Update.Builder(updateAvgRating).index(index).type(userType).id(info[0]).build());

                if (!result.isSucceeded()) {
                    Log.i("Error", "Elasticsearch failed to update user rating");
                    return false;
                }

            } catch (Exception e) {
                Log.i("Error", "Failed to update user rating");
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    private static void verifySettings() {
        // Initialize client if necessary
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://35.164.200.4:9200");
            DroidClientConfig config = builder.build();
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
