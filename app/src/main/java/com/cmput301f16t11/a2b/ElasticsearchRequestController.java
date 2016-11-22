package com.cmput301f16t11.a2b;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
 * Controller for all queries and updates to the elasticsearch server regarded UserRequest objects.
 */

public class ElasticsearchRequestController {
    private static JestDroidClient client;
    private static String index = "f16t11";

    private static String openRequest = "openRequest";
    private static String closedRequest = "closedRequest";
    private static String inProgress = "inProgress";


    /**
     * Adds an open request to elastic search server
     */
    public static class AddOpenRequestTask extends AsyncTask<UserRequest, Void, Boolean> {
        /**
         * Adds a UserRequest object to the openRequest list on the server
         *
         * @param requests the UserRequest obj in question
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(UserRequest... requests) {
            verifySettings();

            Index userIndex = new Index.Builder(requests[0]).index(index).type(openRequest).build();

            try {
                DocumentResult result = client.execute(userIndex);
                if (result.isSucceeded()) {
                    requests[0].setId(result.getId());
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
     * Get the 100 latest closed requests for a rider
     */

    public static class GetPastRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        /**
         * Gets the 100 latest closed requests made by a rider
         * Gets the 100 latest closed requests made by a rider
         *
         * @param user the rider in question
         * @return ArrayList of the UserRequest objs in question
         */
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> riderList = new ArrayList<UserRequest>();
            String search_string =  "{\n" +
                    "    \"query\" : {\n" +
                    "        \"constant_score\" : {\n" +
                    "            \"filter\" : {\n" +
                    "                \"term\" : {\n" +
                    "                    \"riderId\":\""+ user[0] +"\"\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(closedRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    riderList.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find requests for this rider");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return riderList;
        }
    }

    /**
     * Get open requests for a driver
     * @deprecated b/c if the driver has been set for a request then it should be inProgress or closed
     */
    @Deprecated
    public static class GetActiveDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        /**
         * Gets active requests for a driver
         *
         * @param user the driver
         * @return ArrayList of UserRequest objs in questions
         * @deprecated
         */

        @Deprecated
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"driver.userName\": \"" + user[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(openRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    requestList.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find user requests for driver");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return requestList;
        }
    }


    /**
     * Add a driver acceptance to a request
     * info[0] is the request ID
     * info[1] is the driver ID
     */

    public static class AddDriverAcceptanceToRequest extends AsyncTask<String, Void, Boolean> {
        Context context;

        /**
         * Constructor for AddDriverAcceptanceToRequest
         * @param context current context
         */
        public AddDriverAcceptanceToRequest(Context context) {
            this.context = context;
        }

        /**
         * Adds a driver acceptance to a request
         *
         * @param info id of the driver (string)
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(String... info) {
            verifySettings();


            // update an existing request
            //TO DO: check it the request exists first

            // update script

            String script = "{ \"script\" : \" if (ctx._source.acceptedDriverIds == []) {ctx._source.acceptedDriverIds = [newDriver] } " +
                    "else {ctx._source.acceptedDriverIds += newDriver }\", \"params\" : {\"newDriver\" :\""  + info[1] +"\"}}";


            try {
                DocumentResult result = client.execute(new Update.Builder(script).index(index).type(openRequest).id(info[0]).build());

                if (!result.isSucceeded()) {
                    Log.i("Error", "Failed to find user requests for rider");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();

                return false;
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {
            // Notify the user if we were unable to accept the request
            if (!result) {
                Toast.makeText(context, context.getString(R.string.markerInfo_error), Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    /**
     * Sets a confirmed driver to a UserRequest
     */
    public static class SetConfirmedDriver extends AsyncTask<String, Void, Boolean> {
        Context context;

        /**
         * Constructor for SetConfirmedDriver
         *
         * @param context the current context
         */
        public SetConfirmedDriver(Context context) {
            this.context = context;
        }

        /**
         * Sets a confirmed driver for a UserRequest on the elasticsearch server
         *
         * @param info the user ID of the driver
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(String... info) {
            verifySettings();
            String requestId = info[0];
            String driverId = info[1];

            // update an existing request
            //TO DO: check it the request exists first

            // update script

            String script = "{ \"script\" : \" ctx._source.confirmedDriverId = newDriver }\", \"params\" : "+
                    "{\"newDriver\" : {\"id\":\""  + driverId +"\"}}}";

            try {
                DocumentResult result = client.execute(new Update.Builder(script).index(index).type(openRequest).id(requestId).build());

                if (!result.isSucceeded()) {
                    Log.i("Error", "Failed to find user requests for rider");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();

                return false;
            }

            return true;
        }

        /**
         * Notifies the user if unable to accept the request (with a toast message)
         *
         * @param result true if successful (no message), false otherwise (message sends)
         */
        protected void onPostExecute(Boolean result) {
            // Notify the user if we were unable to accept the request
            if (!result) {
                Toast.makeText(context, context.getString(R.string.markerInfo_error), Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    /**
     * Get the currently open Requests for a rider
     */

    public static class GetActiveRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        /**
         * Gets the current open Requests for a rider
         *
         * @param user the userID of the rider in question
         * @return an ArrayList of UserRequest objs that are active and started by the rider
         */
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string =  "{\n" +
                    "    \"query\" : {\n" +
                    "        \"constant_score\" : {\n" +
                    "            \"filter\" : {\n" +
                    "                \"term\" : {\n" +
                    "                    \"riderId\":\""+ user[0] +"\"\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(openRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    requestList.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find user requests for rider");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return requestList;
        }
    }

    /**
     * Get the closed requests for a driver
     */

    public static class GetPastDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        /**
         * Gets the closed requests for a driver
         *
         * @param user the userID of the driver in question
         * @return ArrayList of closed UserRequest objs by the driver
         */
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> driverList = new ArrayList<UserRequest>();

            String search_string =  "{\n" +
                    "    \"query\" : {\n" +
                    "        \"constant_score\" : {\n" +
                    "            \"filter\" : {\n" +
                    "                \"term\" : {\n" +
                    "                    \"confirmedDriverId\":\""+ user[0] +"\"\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(closedRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    driverList.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find driver requests for driver");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return driverList;
        }
    }

    /**
     * Get the inProgress requests for a rider
     */
    public static class GetInPrgressRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        /**
         * Gets requests in in progress state by the rider
         *
         * @param user the id of the rider
         * @return ArrayList of UserRequest objs that are in progress and created by user
         */
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string =  "{\n" +
                    "    \"query\" : {\n" +
                    "        \"constant_score\" : {\n" +
                    "            \"filter\" : {\n" +
                    "                \"term\" : {\n" +
                    "                    \"riderId\":\""+ user[0] +"\"\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(inProgress)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    requestList.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find user requests for rider");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return requestList;
        }
    }

    /**
     * Gets in progress requests
     */
    public static class GetInPrgressRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        /**
         * Gets in progress requests
         *
         * @param user the userId of the driver
         * @return ArrayList of in progress UserRequests
         */
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match_all\":{}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(inProgress)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = 
                        result.getSourceAsObjectList(UserRequest.class);
                    requestList.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find user requests for driver");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return requestList;
        }
    }



    /**
     * Move a request from open to inProgress
     */
    public static class MoveToInProgresseRequest extends AsyncTask<UserRequest, Void, Boolean> {
        /**
         * Moves a request from open to in progress states on elasticsearch server
         *
         * @param requests UserRequest obj in question
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(UserRequest... requests) {
            verifySettings();

            // clear the list of accepted drivers
            requests[0].clearAcceptedDrivers();

            // Delete the request from the list of open requests
            try {
                DocumentResult result = client.execute(new Delete.Builder(requests[0].getId())
                        .index(index)
                        .type(openRequest)
                        .build());
                if (result.isSucceeded()) {
                    //requests[0].setId(result.getId());
                } else {
                    Log.i("Error", "Elasticsearch failed to delete open request");
                    return false;
                }
            } catch (Exception e) {
                Log.i("Error", "Elasticsearch failed to delete open request");
                e.printStackTrace();
                return false;
            }

            //Add the request to inprogress requests
            Index requestIndex = new Index.Builder(requests[0]).index(index).type(inProgress).id(requests[0].getId()).build();

            try {
                DocumentResult result = client.execute(requestIndex);
                if (result.isSucceeded()) {
                    //requests[0].setId(result.getId());
                } else {
                    Log.i("Error", "Elasticsearch failed to add closed request");
                    return false;
                }
            } catch (Exception e) {
                Log.i("Error", "Elasticsearch failed to add closed request");
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }


    /**
     * Move a request from inProgress to closed
     */
    public static class MoveToClosedRequest extends AsyncTask<UserRequest, Void, Boolean> {
        /**
         * Close a request on the elasticsearch server
         * @param requests UserRequest objs to be closed
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(UserRequest... requests) {
            verifySettings();

            // Delete the request from the list of inprogress requests
            try {
                DocumentResult result = client.execute(new Delete.Builder(requests[0].getId())
                        .index(index)
                        .type(inProgress)
                        .build());
                if (result.isSucceeded()) {
                    //requests[0].setId(result.getId());
                } else {
                    Log.i("Error", "Elasticsearch failed to delete open request");
                    return false;
                }
            } catch (Exception e) {
                Log.i("Error", "Elasticsearch failed to delete open request");
                e.printStackTrace();
                return false;
            }

            //Add the request to closed requests
            Index requestIndex = new Index.Builder(requests[0]).index(index).type(closedRequest).id(requests[0].getId()).build();

            try {
                DocumentResult result = client.execute(requestIndex);
                if (result.isSucceeded()) {
                    //requests[0].setId(result.getId());
                } else {
                    Log.i("Error", "Elasticsearch failed to add closed request");
                    return false;
                }
            } catch (Exception e) {
                Log.i("Error", "Elasticsearch failed to add closed request");
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

    /**
     * Gets the current closed requests
     */
    public static class GetClosedRequests extends
                                    AsyncTask<String, Void, ArrayList<UserRequest>>  {
        /**
         * Gets the current closed requests for the user
         *
         * @param user userID of user in question
         * @return ArrayList of UserRequest objs that are currently closed
         */
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match_all\":{}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(closedRequest)
                    .build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> tmp = result.getSourceAsObjectList(UserRequest.class);
                    requestList.addAll(tmp);
                } else {
                    // No requests found in area
                    Log.i("Error", "Failed to find any requests within in the area");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }
            return requestList;
        }
    }


    /**
     * Gets requests nearby the specified location
     */
    public static class GetNearbyRequests extends AsyncTask<Double, Void, ArrayList<UserRequest>> {
        /**
         * Gets the requests nearby the specified location
         *
         * @param params the specified location
         * @return ArrayList of the UserRequest objs in question
         */
        public ArrayList<UserRequest> doInBackground(Double... params) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();

            String search_string = "{\"query\" : { \"match_all\" : {}}, " +
                    "\"filter\" : { \"geo_distance\" : { \"distance\" : \"3km\", " +
                    "\"startLocation\" : [" + params[0] + ", " + params[1] + "]}}}";


            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(openRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> tmp = result.getSourceAsObjectList(UserRequest.class);
                    requestList.addAll(tmp);
                } else {
                    // No requests found in area
                    Log.i("Error", "Failed to find any requests within in the area");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return requestList;
        }
    }

    /**
     * Gets nearby requests using geo distance filter
     */
    public static class GetNearbyRequestsGeoFilter extends AsyncTask<Double, Void, ArrayList<UserRequest>> {
        /**
         * Gets nearby requests using geo distance filter
         * @param params specified location
         * @return ArrayList of UserRequest objects nearby
         */
        public ArrayList<UserRequest> doInBackground(Double... params) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();

            String search_string = "{ \"query\":{\n" +
                    "    \"filtered\" : {\n" +
                    "        \"query\" : {\n" +
                    "            \"match_all\" : {}\n" +
                    "        },\n" +
                    "        \"filter\" : {\n" +
                    "            \"geo_distance\" : {\n" +
                    "                \"distance\" : \""+params[0]+"km\",\n" +
                    "                \"startLocation.location\" : {\n" +
                    "                    \"lat\" : "+ params[1] +",\n" +
                    "                    \"lon\" : "+ params[2] +"}\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(openRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> tmp = result.getSourceAsObjectList(UserRequest.class);
                    requestList.addAll(tmp);
                } else {
                    // No requests found in area
                    Log.i("Error", "Failed to find any requests within in the area");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return requestList;
        }
    }


    /**
     * GetRequest fetches the request with the given Id from the server
     */
    public static class GetOpenRequestById extends AsyncTask<String, Void, UserRequest> {
        /**
         * fetches the request with the given id from the server
         *
         * @param requestID id of the request in question
         * @return UserRequest obj corresponding to requestID
         */
        @Override
        protected UserRequest doInBackground(String... requestID) {
            verifySettings();

            Get get = new Get.Builder(index,requestID[0]).type(openRequest).build();

            UserRequest userRequest = null;

            try {
                JestResult result = client.execute(get);
                if (result.isSucceeded()) {
                    userRequest = result.getSourceAsObject(UserRequest.class);
                }else{
                    Log.i("Error","Filed to find request");
                    return null;
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return null;
            }

            return userRequest;
        }
    }

    /**
     * Get a rider's requests that have been accepted by a driver
     */
    public static class GetAcceptedRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        /**
         * Get a rider's requests that have been accepted by a driver
         *
         * @param user the id of the rider who created requests
         * @return ArrayList of the rider's UserRequests that have been accepted by >=1 driver
         */
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();

            String search_string = "{\n" +
                    "    \"query\": {\n" +
                    "        \"constant_score\" : {\n" +
                    "            \"filter\" : {\n" +
                    "                 \"bool\" : {\n" +
                    "                    \"must\" : [\n" +
                    "                        { \"term\" : { \"riderId\":\"" + user[0] + "\"} }, \n" +
                    "                        { \"term\" : { \"accepted\":true } } \n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";


            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(inProgress)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    accepted.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find any accepted requests");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return accepted;
        }
    }

    /**
     * Get a driver's inProgress Requests
     */

    public static class GetAcceptedDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        /**
         * Get a driver's in progress requests
         *
         * @param user id of the driver
         * @return ArrayList of UserRequests that the driver is the confirmed driver of
         */
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();
            String search_string =  "{\n" +
                    "    \"query\" : {\n" +
                    "        \"constant_score\" : {\n" +
                    "            \"filter\" : {\n" +
                    "                \"term\" : {\n" +
                    "                    \"confirmedDriverId\":\""+ user[0] +"\"\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(inProgress)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    accepted.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find any accepted requests for driver");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return accepted;
        }
    }

    /**
     * Delete a rider request
     */
    public static class DeleteRiderRequests extends AsyncTask<String, Void, Boolean> {
        /**
         * Deletes a rider request
         *
         * @param request the requestID of the request to be deleted
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(String... request) {
            verifySettings();

            try {
                client.execute(new Delete.Builder(request[0])
                        .index(index)
                        .type("openRequest")
                        .build());

        } catch (Exception e) {
            Log.i("Error", "Failed to communicate with elasticsearch server");
            e.printStackTrace();
            return false;
        }

        return true;
        }
    }



    /**
     * Get users who have accepted the request
     */
    public static class getAcceptedUsersForRequest extends AsyncTask<String, Void, ArrayList<String>>{

        /**
         * Get users who have accepted the request
         *
         * @param requestId requestID of the request in question
         * @return ArrayList of User objs who have accepted the request corresponding to requestID
         */
        @Override
        protected ArrayList<String> doInBackground(String ... requestId) {
            verifySettings();

            Get get = new Get.Builder(index, requestId[0]).type(openRequest).build();
            UserRequest userRequest;

            try {
                JestResult result = client.execute(get);
                if (result.isSucceeded()) {
                    userRequest = result.getSourceAsObject(UserRequest.class);
                }
                else{
                    Log.i("Error", "Failed to find any accepted requests");
                    return new ArrayList<>();
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return null;
            }

            return userRequest.getAcceptedDrivers();
        }

    }



    public static class GetRequestsByUserNameKeyword extends AsyncTask<String, Void, ArrayList<UserRequest>> {

        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"rider.userName\": \"" + user[0] +
                    "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(openRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    accepted.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find any accepted requests");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return accepted;
        }
    }

    public static class GetRequestsByDescriptionKeyword extends AsyncTask<String, Void, ArrayList<UserRequest>> {

        @Override
        protected ArrayList<UserRequest> doInBackground(String... description) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();
            String search_string = "{\n" +
                    " \"from\" : 0, \"size\" : 20,\n" +
                    "    \"query\": {\n" +
                    "        \"match\": {\n" +
                    "            \"description\": \"" + description[0] + "\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}'";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(openRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    accepted.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find any accepted requests");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return accepted;
        }
    }

    public static class GetRequestsByStartLocationKeyword extends AsyncTask<String, Void, ArrayList<UserRequest>> {

        @Override
        protected ArrayList<UserRequest> doInBackground(String... description) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();
            String search_string = "{\n" +
                    " \"from\" : 0, \"size\" : 20,\n" +
                    "    \"query\": {\n" +
                    "        \"match\": {\n" +
                    "            \"startLocation\": \"" + description[0] + "\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}'";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(openRequest)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<UserRequest> foundRequests = result.getSourceAsObjectList(UserRequest.class);
                    accepted.addAll(foundRequests);
                } else {
                    Log.i("Error", "Failed to find any accepted requests");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return accepted;
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
