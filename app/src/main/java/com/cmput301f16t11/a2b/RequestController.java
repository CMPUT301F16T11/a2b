package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;

import static com.cmput301f16t11.a2b.Mode.DRIVER;
import static com.cmput301f16t11.a2b.UserController.saveInFile;

/**
 * Created by brianofrim on 2016-10-13.
 */
public class RequestController {


    public static ArrayList<UserRequest> getRequestNear(String address, Number radius){
        return new ArrayList<UserRequest>();
    }

    public static ArrayList<UserRequest> getRequestNear(LatLng location, Number radius) {
        return new ArrayList<UserRequest>();
    }

    static public void runBackgroundTasks(String usr, Activity activity, Boolean saveAfter) {

        ElasticsearchRequestController.GetPastRiderRequests riderTask =
                new ElasticsearchRequestController.GetPastRiderRequests();
        ElasticsearchRequestController.GetPastDriverRequests driverTask =
                new ElasticsearchRequestController.GetPastDriverRequests();
        ElasticsearchRequestController.GetActiveRiderRequests currRiderTask =
                new ElasticsearchRequestController.GetActiveRiderRequests();
        ElasticsearchRequestController.GetActiveDriverRequests currDriverTask =
                new ElasticsearchRequestController.GetActiveDriverRequests();
        riderTask.execute(usr);
        driverTask.execute(usr);
        try {
            UserController.setClosedRequestsAsRider(riderTask.get());
            UserController.setClosedRequestsAsDriver(driverTask.get());
            UserController.setActiveRequestsAsRider(currRiderTask.get());
            //TODO something wrong here
           // UserController.setActiveRequestsAsDriver(currRiderTask.get());

        } catch (Exception e) {
            Log.i("Error", "AsyncTask failed to execute");
            e.printStackTrace();
        }

        // Saves user file after completion of asyncTasks if necessary
        if (saveAfter) {
            saveInFile(activity);
        }
    }

    // TEMPORARY POPULATION OF RANDOM REQUESTS!!!
    public static ArrayList<UserRequest> tempFakeRequestList() {
        ArrayList<UserRequest> returnValue = new ArrayList<UserRequest>();
        returnValue.add(new UserRequest(new LatLng(53.5443890, -113.4909270),
                new LatLng(54.07777, -113.50192), 10.01, "test"));
        returnValue.add(new UserRequest(new LatLng(54.07777, -113.50192),
                new LatLng(53.5443890, -113.4909270), 13.31, "test1"));
        returnValue.add(new UserRequest(new LatLng(54.07777, -113.50192),
                new LatLng(53.5443890, -113.4909270), 18.31, "test2"));
        return returnValue;
    }

    public static ArrayList<UserRequest> getNearbyRequests(LatLng location, int radius) {
        /**
         * For use in ride or drive mode
         * Gets all requests nearby to current location
         */
        ElasticsearchRequestController.GetNearbyRequests searchController = new ElasticsearchRequestController.GetNearbyRequests();
        ArrayList<UserRequest> nearBy = searchController.doInBackground(location.latitude - radius, location.longitude - radius, location.latitude + radius, location.longitude + radius);
        //return RequestController.tempFakeRequestList(); // for testing
        return nearBy;
    }

    public static ArrayList<UserRequest> getOwnActiveRequests(User user) {
        /**
         * For use in ride mode only
         * Gets all requests created by the user
         */
        ArrayList<UserRequest> userRequests;

        if (UserController.checkMode() == DRIVER) {
            ElasticsearchRequestController.GetActiveDriverRequests searchController = new ElasticsearchRequestController.GetActiveDriverRequests();
            userRequests = searchController.doInBackground(user.getName());
            return userRequests;
        }
        else {
            ElasticsearchRequestController.GetActiveRiderRequests searchController = new ElasticsearchRequestController.GetActiveRiderRequests();
            userRequests = searchController.doInBackground(user.getName());
            return userRequests;
        }
    }

    public static ArrayList<UserRequest> getOwnUnactiveRequests(User user) {
        ArrayList<UserRequest> userRequests;

        if(UserController.checkMode() == DRIVER) {
            ElasticsearchRequestController.GetPastDriverRequests searchController = new ElasticsearchRequestController.GetPastDriverRequests();
            userRequests = searchController.doInBackground(user.getName());
            return userRequests;
        }
        else {
            ElasticsearchRequestController.GetPastRiderRequests searchController = new ElasticsearchRequestController.GetPastRiderRequests();
            userRequests = searchController.doInBackground(user.getName());
            return userRequests;
        }
    }

    public static ArrayList<UserRequest> getAcceptedByDrivers(User user) {
        /**
         * For use in ride mode only. Gets the requests that user has created and
         * are currently accepted by at least one driver.
         * Excludes completed requests.
         */
        // riders only
        //TODO: actual logic
        // (get requests accepted by a driver created by this user
        return new ArrayList<UserRequest>();
    }

    public static ArrayList<UserRequest> getAcceptedByUser(User user) {
        /**
         * For use in driver mode only. Gets the requests that user has currently
         * accepted, excluding completed requests.
         */
        // drivers only
        // TODO: actual logic
        // (get requests accepted by the curr user)
        ArrayList<UserRequest> tmp = RequestController.tempFakeRequestList();
        tmp.remove(2);
        return tmp;
    }

    public static ArrayList<UserRequest> getCompletedRequests(User user, Mode mode) {
        // TODO: actual logic
        /**
         * Gets the completed requests BY a driver if mode == Mode.DRIVER
         * Gets the completed requests a rider received if mode == Mode.RIDER
         */
    return new ArrayList<UserRequest>();
    }

    public static ArrayList<UserRequest> getConfirmedByRiders(User user, Mode mode) {
        //TODO: actual logic
        /**
        * get request accepted by the current user as a driver, that are also accepted by the user
        * who originally made the request
        * OR if the curr user is a rider, check the requests they have confirmed
         * Excludes completed requests
        */
        ArrayList<UserRequest> temp = new ArrayList<UserRequest>();
        temp.add(RequestController.tempFakeRequestList().get(2));
        return temp;
    }

    public static Collection<? extends UserRequest> getNearbyRequests(LatLng latLng) {
        return null; //TODO: implement this function
    }

    public static Collection<? extends UserRequest> getOwnRequests(User user) {
        return null; //TODO: implement this function
    }
}
