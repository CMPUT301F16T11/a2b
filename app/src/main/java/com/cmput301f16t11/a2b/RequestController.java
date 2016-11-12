package com.cmput301f16t11.a2b;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;

import static com.cmput301f16t11.a2b.Mode.DRIVER;

/**
 * Request Controller to deal with all local backend Request work
 */
public class RequestController {

    public static ArrayList<UserRequest> nearbyRequests;


    /**
     * Sets the static variable nearbyRequests
     *
     * @param requests ArrayList<UserRequest> to set nearbyRequests to
     */
    public static void setNearbyRequests(ArrayList<UserRequest> requests) {
        nearbyRequests = requests;
    }

    /**
     * gets the static variable nearbyRequests
     *
     * @return ArrayList<UserRequest> nearbyRequests
     */
    public static ArrayList<UserRequest> getNearbyRequests() {
        return nearbyRequests;
    }

    /**
     * Adds an acceptance to the request specified by the current user (driver mode only)
     *
     * @param request UserRequest obj that needs an acceptance added to it
     * @param context current application context
     */
    public static void addAcceptance(UserRequest request, Context context) {
        ElasticsearchRequestController.AddDriverAcceptanceToRequest addAcceptance =
                new ElasticsearchRequestController.AddDriverAcceptanceToRequest(context);
        // request id driver id
        addAcceptance.execute(request.getId(), UserController.getUser().getId());
    }

    /**
     * Uses elasticsearch controller to add an open request
     *
     * @see ElasticsearchRequestController
     * @param request the UserRequest obj to add
     */
    public static void addOpenRequest(UserRequest request) {
        ElasticsearchRequestController.AddOpenRequestTask addOpenRequest =
                new ElasticsearchRequestController.AddOpenRequestTask();
        addOpenRequest.execute(request);
    }

    /**
     * sets the request's confirmed driver
     *
     * @see ElasticsearchRequestController
     *
     * @param request the UserRequest obj to add
     * @param driver the confirmed driver (UserRequest)
     * @param cntxt the current application context
     */
    public static void setRequestConfirmedDriver(UserRequest request, User driver, Context cntxt){
        request.setConfirmedDriver(driver);

        ElasticsearchRequestController.SetConfirmedDriver searchController =
                new ElasticsearchRequestController.SetConfirmedDriver(cntxt);
        searchController.execute(request.getConfirmedDriver().getName());
        completeRequest(request);
    }


    /**
     * Get all open requests within distatnce of lat, lon
     *
     * @see ElasticsearchRequestController
     *
     * @param distance how far away from the point
     * @param lat the lat value of current point (double)
     * @param lon the lon value of the current point (double)
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

    /**
     * Get the user's active requests created by them (rider mode only)
     *
     * @see ElasticsearchRequestController
     *
     * @param user the user in question
     * @return ArrayList<UserRequest> of UserRequests in question
     */
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


    /**
     * Get all UserRequests accepted by at least one driver
     *
     * @see ElasticsearchRequestController
     *
     * @param user the user obj in question
     * @return list of UserRequests accepted by >= 1 driver
     */
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

    /**
     * Gets the request that the user has currently accepted excluding completed
     * NOT WORKING
     *
     * @see ElasticsearchRequestController
     *
     * @param user the driver in question
     * @return List of UserRequests currently accepted
     */
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

    /**
     * Gets the completed requests by a driver (drive mode)
     * gets the completed request a rider created (ride mode)
     *
     * @param user the user in question
     * @param mode drive/ride mode
     * @return list of completed UserRequests
     */
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
     * get all the accepted driver for specfic request
     *
     * @param request request obj you want to get accepted drivers for
     * @return list of Users who have accepted the request
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
     *
     * @param users Arraylist of user object with only ids
     * @return returns a list of all actual user objects
     */
    private static ArrayList<User> getUserInfo(ArrayList<User> users){
        ArrayList<User> actualUserObjects = new ArrayList<>();
        for(User user: users){
            ElasticsearchUserController.RetrieveUserInfo impl =
                    new ElasticsearchUserController.RetrieveUserInfo();
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

    /**
     * get requests accepted by the current user as a driver, that are also accepted by the user
     * who originally made the request
     * OR if the curr user is a rider, check the requests they have confirmed
     * Excludes completed requests
     *
     * @param user the user obj
     * @param mode current mode
     * @return list of UserRequests
     */
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
     *
     * @param ur the UserRequest to be movved
     */
    public static void moveToInProgress(UserRequest ur){
        ElasticsearchRequestController.MoveToInProgresseRequest moveToInProgresseRequest = new ElasticsearchRequestController.MoveToInProgresseRequest();
        moveToInProgresseRequest.execute(ur);

    }

    /**
     * Move a request from inProgress to closed
     *
     * @param ur the UserRequest to be moved
     */

    public static void moveToClosed(UserRequest ur){
        ElasticsearchRequestController.MoveToClosedRequest moveToClosedRequest = new ElasticsearchRequestController.MoveToClosedRequest();
        moveToClosedRequest.execute(ur);

    }

    /**
     * Gets an openrequest by requestID
     * @param id the requestID
     * @return the UserRequest obj corresponding to the id
     */
    public static UserRequest getOpenRequestById(String id){
        ElasticsearchRequestController.GetOpenRequestById getOpenRequestById = new ElasticsearchRequestController.GetOpenRequestById();
        UserRequest ur = null;
        try{
            ur =getOpenRequestById.execute(id).get();
        }catch(Exception e){

        }
        return ur;

    }

    /**
     * Deletes a request
     *
     * @see ElasticsearchRequestController
     * @param id the id of the request to be deleted
     * @return true if successful, false otherwise
     */
    public static Boolean deleteRequest(String id) {
        ElasticsearchRequestController.DeleteRiderRequests deleteRequestsById =
                new ElasticsearchRequestController.DeleteRiderRequests();
        try {
            deleteRequestsById.execute(id);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Completes a request
     *
     * @see ElasticsearchRequestController
     * @param request the request obj to be completed
     */
    public static void completeRequest(UserRequest request) {
        // local updates
        request.setCompletedStatus(true);

        // server updates
        ElasticsearchRequestController.MoveToClosedRequest searchController =
                new ElasticsearchRequestController.MoveToClosedRequest();
        searchController.execute(request);

    }
}
