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

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Wilky on 11/6/2016.
 */

public class ElasticsearchRequestController {
    private static JestDroidClient client;
    private static String index = "f16t11";

    private static String openRequest = "openRequest";
    private static String closedRequest = "closedRequest";


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
                    Log.i("Error", "Failed to find requests for this rideR");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return riderList;
        }
    }

    public static class GetActiveUserRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
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
                    Log.i("Error", "Failed to find user requests for user");
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
                    Log.i("Error", "Failed to find driver requests for user");
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return driverList;
        }
    }


//    public static class GetActiveRequestsInRange extends AsyncTask<String, Void, ArrayList<UserRequest>> {
//        @Override
//        // Kind of a hack but
//        //info[0] = lat
//        //info[1] = lon
//        //info[2] = distance in decimal km
//        protected ArrayList<UserRequest> doInBackground(String... info) {
//            verifySettings();
//
//            ArrayList<UserRequest> activeRequestsInRange;
//            String query_stirng = "{\n" +
//                    "    \"filtered\" : {\n" +
//                    "        \"query\" : {\n" +
//                    "            \"match_all\" : {}\n" +
//                    "        },\n" +
//                    "        \"filter\" : {\n" +
//                    "            \"geo_distance\" : {\n" +
//                    "                \"distance\" : \""+ info[2] +"\",\n" +
//                    "                \"pin.location\" : {\n" +
//                    "                    \"lat\" : "+info[0] +",\n" +
//                    "                    \"lon\" : "+info[1] +"\n" +
//                    "                }\n" +
//                    "            }\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "}";
//
//
//
//        }
//
//    }

//    public static class AddOpenRequestTask extends AsyncTask<User, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Request... requests) {
//            verifySettings();
//
//            Index userIndex = new Index.Builder(requests[0]).index(index).type(openRequest).build();
//
//            try {
//                DocumentResult result = client.execute(userIndex);
//                if (result.isSucceeded()) {
//                    requests[0].setId(result.getId());
//                } else {
//                    Log.i("Error", "Elasticsearch failed to add user");
//                    return false;
//                }
//            } catch (Exception e) {
//                Log.i("Error", "Failed to add user to elasticsearch");
//                e.printStackTrace();
//                return false;
//            }
//
//            return true;
//        }
//    }
    /**
     * Move a request from open to closed
     *
     */

    public static class CloseRequest extends AsyncTask<UserRequest, Void, Boolean> {
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
                }
            } catch (Exception e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
            }

            return requestList;
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
