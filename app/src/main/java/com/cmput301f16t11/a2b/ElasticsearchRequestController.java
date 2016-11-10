package com.cmput301f16t11.a2b;

import android.os.AsyncTask;
import android.util.Log;

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
     *
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

    public static class GetPastRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> riderList = new ArrayList<UserRequest>();
            String search_string = "{\"from\":0. \"to\":100, \"query\": {\"match\": {\"rider\": \"" + user[0] + "\"}}}";

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

    public static class GetActiveDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"driver\": \"" + user[0] + "\"}}}";

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
        @Override
        protected Boolean doInBackground(String... info) {
            verifySettings();


            // update an existing request
            //TO DO: check it the request exists first

            // update script

            String script = "{ \"script\" : \"ctx._source.acceptedDrivers += newDriver \", \"params\" : {\"newDriver\" : {\"id\":\""  + info[1] +"\"}}}";


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
    }

    public static class GetActiveRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"rider\": \"" + user[0] + "\"}}}";

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

    public static class GetPastDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> driverList = new ArrayList<UserRequest>();
            String search_string = "{\"from\": 0, \"to\": 100, \"query\": { \"match\": {\"driver\": \"" + user[0] + "\"}}}";

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


    public static class GetInPrgressRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> requestList = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"rider\": \"" + user[0] + "\"}}}";

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
     * Move a request from open to inprogress
     *(Untested)
     *
     *
     */


    public static class MoveToInProgresseRequest extends AsyncTask<UserRequest, Void, Boolean> {
        @Override
        protected Boolean doInBackground(UserRequest... requests) {
            verifySettings();

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
     * Move a request from inporgress to closed
     *(Untested)
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

            String search_string = "{\"query\": { \"range\" : { \"startLocation\" { \"latitude\" : { \"gte\" : \""
                    + params[0] + "\". \"lte\" : \"" + params[1] + "\"}. \"longitude\": { \"gte\" : \""
                    + params[2] + "\". \"lte\" : \"" + params[3] + "\"}}}}}";

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
     * param[0] - distance
     * param[1] - lat
     * param[2] -lon
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
     * takes in a request id
     * returns the request with that ID, null if the request does not exist
     */

    public static class GetRequest extends AsyncTask<String, Void, UserRequest> {
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



    public static class GetAcceptedRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"rider\": \"" + user[0] + "\", \"accepted\" : \"true\"}}";

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


    public static class GetAcceptedDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> accepted = new ArrayList<UserRequest>();
            String search_string = "{\"query\": { \"match\": {\"driver\": \"" + user[0] + "\", \"accepted\" : \"true\"}}";

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
