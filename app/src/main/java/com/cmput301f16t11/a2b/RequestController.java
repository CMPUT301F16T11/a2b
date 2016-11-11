package com.cmput301f16t11.a2b;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;

import static com.cmput301f16t11.a2b.Mode.DRIVER;

/**
 * Created by brianofrim on 2016-10-13.
 */
public class RequestController {

    public static ArrayList<UserRequest> nearbyRequests;


    public static ArrayList<UserRequest> getRequestNear(String address, Number radius){
        return new ArrayList<UserRequest>();
    }

    public static ArrayList<UserRequest> getRequestNear(LatLng location, Number radius) {
        return new ArrayList<UserRequest>();
    }

    public static void setNearbyRequests(ArrayList<UserRequest> requests) {
        nearbyRequests = requests;
    }

    public static ArrayList<UserRequest> getNearbyRequests() {
        return nearbyRequests;
    }

    public static void addAcceptance(UserRequest request, Context context) {
        ElasticsearchRequestController.AddDriverAcceptanceToRequest addAcceptance =
                new ElasticsearchRequestController.AddDriverAcceptanceToRequest(context);
        // request id driver id
        addAcceptance.execute(request.getId(), UserController.getUser().getId());
    }


    public static void addOpenRequest(UserRequest request) {
        ElasticsearchRequestController.AddOpenRequestTask addOpenRequest =
                new ElasticsearchRequestController.AddOpenRequestTask();
        addOpenRequest.execute(request);
    }

    public static void setRequestConfirmedDriver(UserRequest request, User driver, Context cntxt){
        request.setConfirmedDriver(driver);

        ElasticsearchRequestController.SetConfirmedDriver searchController =
                new ElasticsearchRequestController.SetConfirmedDriver(cntxt);
        searchController.execute(request.getConfirmedDriver().getName());
        completeRequest(request);
    }


    /**
     * Get all open requests within distatnce of lat, lon
     */
    public static ArrayList<UserRequest> getNearbyRequestsGeoFilter(Double distance, Double lat, Double lon){
        ElasticsearchRequestController.GetNearbyRequestsGeoFilter getNearbyRequestsGeoFilter = new ElasticsearchRequestController.GetNearbyRequestsGeoFilter();
        ArrayList<UserRequest> nearbyRequests = new ArrayList<UserRequest>();
        try{
            nearbyRequests = getNearbyRequestsGeoFilter.execute(distance,lat,lon).get();
        }catch(Exception e){
            Log.i("Error", "Failiure");
            e.printStackTrace();
        }

        return  nearbyRequests;
    }

    public static ArrayList<UserRequest> getNearbyRequests(LatLng location, int radius) {
        /**
         * For use in ride or drive mode
         * Gets all requests nearby to current location
         */
        ElasticsearchRequestController.GetNearbyRequests searchController =
                new ElasticsearchRequestController.GetNearbyRequests();
        ArrayList<UserRequest> nearBy = new ArrayList<UserRequest>();
        try {
            nearBy = searchController.execute(
                    location.latitude - radius, location.longitude - radius,
                    location.latitude + radius, location.longitude + radius).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nearbyRequests = nearBy;
        return nearBy;
    }

    public static ArrayList<UserRequest> getOwnActiveRequests(User user) {
        /**
         * For use in ride mode only
         * Gets all requests created by the user
         */
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();

        if (UserController.checkMode() == DRIVER) {
            ElasticsearchRequestController.GetActiveDriverRequests searchController = new ElasticsearchRequestController.GetActiveDriverRequests();

            try {
                userRequests = searchController.execute(user.getName()).get();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        else {
            ElasticsearchRequestController.GetActiveRiderRequests searchController = new ElasticsearchRequestController.GetActiveRiderRequests();
            try{
                userRequests = searchController.execute(user.getName()).get();
            }catch(Exception e){

            }

        }
        return userRequests;
    }

    public static ArrayList<UserRequest> getOwnUnactiveRequests(User user) {
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();
        if(UserController.checkMode() == DRIVER) {
            ElasticsearchRequestController.GetPastDriverRequests searchController =
                    new ElasticsearchRequestController.GetPastDriverRequests();
            userRequests = new ArrayList<UserRequest>();
            try {
                userRequests = searchController.execute(user.getName()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return userRequests;
        }
        else {
            ElasticsearchRequestController.GetPastRiderRequests searchController =
                    new ElasticsearchRequestController.GetPastRiderRequests();
            try {
                userRequests = searchController.execute(user.getName()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();
        ElasticsearchRequestController.GetAcceptedRequests searchController =
                new ElasticsearchRequestController.GetAcceptedRequests();
        try {
            userRequests = searchController.execute(user.getName()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userRequests;
    }

    public static ArrayList<UserRequest> getAcceptedByUser(User user) {
        /**
         * For use in driver mode only. Gets the requests that user has currently
         * accepted, excluding completed requests.
         */
        // drivers only
        // (get requests accepted by the curr user)
        ElasticsearchRequestController.GetAcceptedDriverRequests searchController =
                new ElasticsearchRequestController.GetAcceptedDriverRequests();
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest> ();
        try {
            userRequests = searchController.execute(user.getName()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userRequests;
    }

    public static ArrayList<UserRequest> getCompletedRequests(User user, Mode mode) {
        /**
         * Gets the completed requests BY a driver if mode == Mode.DRIVER
         * Gets the completed requests a rider received if mode == Mode.RIDER
         */
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest> ();
        ArrayList<UserRequest> temp = new ArrayList<UserRequest>();
        ElasticsearchRequestController.GetClosedRequests searchController =
                new ElasticsearchRequestController.GetClosedRequests();

        try {
            userRequests = searchController.execute(user.getName()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        temp.addAll(userRequests);
        for (UserRequest request: temp) {
            if (mode == Mode.DRIVER) {
                if (!request.getConfirmedDriver().equals(user)) {
                    userRequests.remove(request);
                }
            }
            else if (mode == Mode.RIDER) {
                if (!request.getRider().equals(user)) {
                    userRequests.remove(request);
                }
            }
        }
        return userRequests;
    }

    /**
     * get all the accepted driver for specfic request id
     * @param request request you want to get accepted drivers for
     * @return
     */
    public static ArrayList<User> getAcceptedDrivers(UserRequest request) {
        ElasticsearchRequestController.getAcceptedUsersForRequest searchController =
                new ElasticsearchRequestController.getAcceptedUsersForRequest();
        ArrayList<User> users;
        try {
            users = searchController.execute(request.getId()).get();
             return getUserInfo(users);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ArrayList<>();
        }
    }

    /**
     * Gets the actual object of all user ignore the request ids
     * @param users Arraylist of user object with only ids
     * @return returns a list of all actual user objects
     */
    private static ArrayList<User> getUserInfo(ArrayList<User> users){
        ArrayList<User> actualUserObjects = new ArrayList<>();
        for(User user: users){
            ElasticsearchUserController.RetriveUserInfo impl =
                    new ElasticsearchUserController.RetriveUserInfo();
            User actualUser;
            try {
                actualUser = impl.execute(user).get();
                actualUserObjects.add(actualUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return actualUserObjects;
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
        if (mode == Mode.RIDER) {
            ElasticsearchRequestController.GetInPrgressRiderRequests searchController =
                    new ElasticsearchRequestController.GetInPrgressRiderRequests();
            try {
                temp = searchController.execute(user.getName()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            // driver mode
            ElasticsearchRequestController.GetInPrgressRequests searchController =
                    new ElasticsearchRequestController.GetInPrgressRequests();
            try {
                temp = searchController.execute(user.getName()).get();
                ArrayList<UserRequest> temp_copy = new ArrayList<UserRequest>();
                temp_copy.addAll(temp);
                for (UserRequest request: temp_copy) {
                    if (!request.getAcceptedDrivers().contains(user)) {
                        temp.remove(request);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    /**
     * Move a request from open to inProgress
     */
    public static void moveToInProgress(UserRequest ur){
        ElasticsearchRequestController.MoveToInProgresseRequest moveToInProgresseRequest = new ElasticsearchRequestController.MoveToInProgresseRequest();
        moveToInProgresseRequest.execute(ur);

    }

    /**
     * Move a request from inProgress to closed
     */

    public static void moveToClosed(UserRequest ur){
        ElasticsearchRequestController.MoveToClosedRequest moveToClosedRequest = new ElasticsearchRequestController.MoveToClosedRequest();
        moveToClosedRequest.execute(ur);

    }

    public static UserRequest getOpenRequestById(String id){
        ElasticsearchRequestController.GetOpenRequestById getOpenRequestById = new ElasticsearchRequestController.GetOpenRequestById();
        UserRequest ur = null;
        try{
            ur =getOpenRequestById.execute(id).get();
        }catch(Exception e){

        }
        return ur;

    }

    public static Boolean deleteRequest(String id) {
        ElasticsearchRequestController.deleteRiderRequests deleteRequestsById = new ElasticsearchRequestController.deleteRiderRequests();
        try {
            deleteRequestsById.execute(id);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void completeRequest(UserRequest request) {
        // local updates
        request.setCompletedStatus(true);

        // server updates
        ElasticsearchRequestController.MoveToClosedRequest searchController =
                new ElasticsearchRequestController.MoveToClosedRequest();
        searchController.execute(request);

    }
}
