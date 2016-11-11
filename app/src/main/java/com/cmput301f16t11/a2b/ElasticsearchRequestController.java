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

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;


/**
 * Created by Wilky on 11/6/2016.
 */

public class ElasticsearchRequestController {
    private static JestDroidClient client;
    private static String index = "f16t11";

    private static String openRequest = "openRequest";
    private static String closedRequest = "closedRequest";
    private static String inProgress = "inProgress";


    /**
     * Add an open request to elastic search server
     * input - UserRequest
     * output - Boolean of if it was added
     */

    public static class AddOpenRequestTask extends AsyncTask<UserRequest, Void, Boolean> {
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
     * input - userName of the rider
     * output - ArrayList<UserRequest>
     */

    public static class GetPastRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> riderList = new ArrayList<UserRequest>();
            String search_string = "{\"from\":0. \"to\":100, \"query\": {\"match\": {\"rider.userName\": \"" + user[0] + "\"}}}";

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
     * @depticated b/c if the driver has been set for a request then it should be inProgress or closed
     */
    @Deprecated
    public static class GetActiveDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
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

        public AddDriverAcceptanceToRequest(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... info) {
            verifySettings();


            // update an existing request
            //TO DO: check it the request exists first

            // update script

            String script = "{ \"script\" : \" if (ctx._source.acceptedDrivers == null) {ctx._source.acceptedDrivers = [newDriver] } " +
                    "else {ctx._source.acceptedDrivers += newDriver }\", \"params\" : {\"newDriver\" : {\"id\":\""  + info[1] +"\"}}}";


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

    public static class SetConfirmedDriver extends AsyncTask<String, Void, Boolean> {
        Context context;

        public SetConfirmedDriver(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... info) {
            verifySettings();


            // update an existing request
            //TO DO: check it the request exists first

            // update script

            String script = "{ \"script\" : \" ctx._source.confirmedDriver = newDriver }\", \"params\" : "+
                    "{\"newDriver\" : {\"id\":\""  + info[1] +"\"}}}";


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
     * Get the currently open Requests for a rider
     * input - userName of the rider
     * output - ArrayList<UserRequest>
     */

    public static class GetActiveRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"rider.userName\": \"" + user[0] + "\"}}}";

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
     * input - userName of the driver
     * output - ArrayList<UserRequest>
     */

    public static class GetPastDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> driverList = new ArrayList<UserRequest>();
            String search_string = "{\"from\": 0, \"to\": 100, \"query\": { \"match\": {\"driver.userName\": \"" + user[0] + "\"}}}";

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
     * input - userName of the rider
     * output - ArrayList<UserRequest>
     */


    public static class GetInPrgressRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"rider.userName\": \"" + user[0] + "\"}}}";

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

    public static class GetInPrgressRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
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
     * input - requests[0]
     * output - Boolean of if the move worked
     */


    public static class MoveToInProgresseRequest extends AsyncTask<UserRequest, Void, Boolean> {
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
     * input - requests[0]
     * output - Boolean of if the move worked
     */

    public static class MoveToClosedRequest extends AsyncTask<UserRequest, Void, Boolean> {
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

    public static class GetClosedRequests extends
                                    AsyncTask<String, Void, ArrayList<UserRequest>>  {
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
     * Get the id of the first hit in jsonResponse
     *
     * @param jsonResponse
     * @return string
     */
    private static String  getIdFromResult(JsonObject jsonResponse){
        JsonObject hits = jsonResponse.getAsJsonObject("hits");
        JsonArray actualHits = hits.getAsJsonArray("hits");
        JsonObject firstHit = actualHits.get(0).getAsJsonObject();
        String id = firstHit.get("_id").toString();
        return id;
    }


    public static class GetNearbyRequests extends AsyncTask<Double, Void, ArrayList<UserRequest>> {
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
     * Same as above but uses geo distance filter
     * Get all active requests that are within distance of lat,lon
     * Input - param[0] - distance, param[1] - lat, param[2] - lon
     * Output - ArrayList<UserRequest>
     */

    public static class GetNearbyRequestsGeoFilter extends AsyncTask<Double, Void, ArrayList<UserRequest>> {
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
     *
     * input - String (request id)
     * output - UserRequest
     */

    public static class GetOpenRequestById extends AsyncTask<String, Void, UserRequest> {
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
     * input - String(Rider's userName)
     * output - ArrayList<UserRequest>
     */

    public static class GetAcceptedRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": [{\"rider.userName\": \"" + user[0] +
                    "\"}, {\"accepted\": true}]}}";

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
     * Get a driver's inPorgress Requests
     * input - the driver's userName
     * output - ArrayList<UserRequest>
     */

    public static class GetAcceptedDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"driver.userName\": \"" + user[0] + "\"}}}";

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

    public static class deleteRiderRequests extends AsyncTask<String, Void, Boolean> {
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

    public static class getAcceptedUsersForRequest extends AsyncTask<String, Void, ArrayList<User>>{

        @Override
        protected ArrayList<User> doInBackground(String ... requestId) {
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
}
