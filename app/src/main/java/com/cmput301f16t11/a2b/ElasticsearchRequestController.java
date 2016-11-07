package com.cmput301f16t11.a2b;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Wilky on 11/6/2016.
 */

public class ElasticsearchRequestController {
    private static JestDroidClient client;
    private static String index = "f16t11";
    private static String requestType = "closedRequest";

    public static class GetPastRiderRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> riderList = new ArrayList<UserRequest>();
            String search_string = "{\"from\":0. \"to\":100, \"query\": {\"match\": {\"rider\": \"" + user[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(requestType)
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

    public static class GetPastDriverRequests extends AsyncTask<String, Void, ArrayList<UserRequest>> {
        @Override
        protected ArrayList<UserRequest> doInBackground(String... user) {
            verifySettings();

            ArrayList<UserRequest> driverList = new ArrayList<UserRequest>();
            String search_string = "{\"from\": 0, \"to\": 100, \"query\": { \"match\": {\"driver\": \"" + user[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(index)
                    .addType(requestType)
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
