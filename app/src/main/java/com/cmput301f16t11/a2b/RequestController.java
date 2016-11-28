package com.cmput301f16t11.a2b;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.cmput301f16t11.a2b.Mode.DRIVER;

/**
 * Request Controller to deal with all local backend Request work
 */
public class RequestController {

    public static ArrayList<UserRequest> displayedRequests;
    public static UserRequest deletedRequest;

    // FileNames
    final static String riderRequests = "riderOwnerRequests.sav";
    final static String nearbyRequests = "nearby.sav";
    final static String acceptedByDriver = "acceptedByMe.sav";
    final static String completedByDriver = "completedRequestsDriver.sav";


    /**
     * Sets the static variable nearbyRequests
     *
     * @param requests ArrayList<UserRequest> to set nearbyRequests to
     */
    public static void setDisplayedRequests(ArrayList<UserRequest> requests, Context con) {
        displayedRequests = requests;
        FileController.saveInFile(requests, nearbyRequests, con);
    }

    /**
     * gets the static variable nearbyRequests
     *
     * @return ArrayList<UserRequest> nearbyRequests
     */
    public static ArrayList<UserRequest> getNearbyRequests() {

        return RequestController.getDisplayedRequests();
    }

    /**
     * To be run on startup to ensure backed up data is loaded
     *
     * @param context application context
     */
    public static void loadDisplayedRequests(Context context) {
        displayedRequests = FileController.loadFromFile(nearbyRequests, context);
    }

    /**
     * Gets the static variable displayedrequests
     *
     * @return arraylist of userrequest objects
     */
    public static ArrayList<UserRequest> getDisplayedRequests() {
        if (displayedRequests == null) {
            return new ArrayList<UserRequest>();
        }
        return displayedRequests;
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
        // update saved file
        ArrayList<UserRequest> requests = new ArrayList<UserRequest>();
        requests = FileController.loadFromFile(acceptedByDriver, context);
        requests.add(request);
        FileController.saveInFile(requests, acceptedByDriver, context);
    }

    public static void addAcceptanceOffline(UserRequest request, Context context) {
        // update saved file
        ArrayList<UserRequest> requests = new ArrayList<UserRequest>();
        requests = FileController.loadFromFile(acceptedByDriver, context);
        requests.add(request);
        FileController.saveInFile(requests, acceptedByDriver, context);
        ElasticsearchRequestController.AddDriverAcceptanceToRequestOffline addAcceptance =
                new ElasticsearchRequestController.AddDriverAcceptanceToRequestOffline();
        // request id driver id
        addAcceptance.execute(request.getId(), UserController.getUser().getId());

    }

    /**
     * Uses elasticsearch controller to add an open request
     *
     * @see ElasticsearchRequestController
     * @param request the UserRequest obj to add
     */
    public static void addOpenRequest(UserRequest request, Context context) {
        ElasticsearchRequestController.AddOpenRequestTask addOpenRequest =
                new ElasticsearchRequestController.AddOpenRequestTask();
        try{
            addOpenRequest.execute(request).get();
        }
        catch(Exception e){
            e.printStackTrace();
        }


        ArrayList<UserRequest> requests = new ArrayList<UserRequest>();
        requests = FileController.loadFromFile(riderRequests, context);
        requests.add(request);
        FileController.saveInFile(requests, riderRequests, context);
    }

    public static void addBatchOpenRequests(ArrayList<UserRequest> requests, Context con){
        ElasticsearchRequestController.AddBatchOpenRequestTask addBatchOpenRequestTask =
                new ElasticsearchRequestController.AddBatchOpenRequestTask();
        try{
            addBatchOpenRequestTask.execute(requests).get();
        }catch(Exception e){
            e.printStackTrace();
        }

        // Save in File
        ArrayList<UserRequest> allRequests = FileController.loadFromFile(riderRequests, con);
        // add new requests to the master list
        for(UserRequest request : requests) {
            allRequests.add(request);
        }
        FileController.saveInFile(allRequests, riderRequests, con);
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

        request.setConfirmedDriver(driver.getId());
        ElasticsearchRequestController.MoveToInProgresseRequest moveController = new  ElasticsearchRequestController.MoveToInProgresseRequest();
        moveController.execute(request);

        try{
            if(moveController.get()){
                ElasticsearchRequestController.SetConfirmedDriver searchController = new ElasticsearchRequestController.SetConfirmedDriver(cntxt);
                searchController.execute(request.getId(), driver.getId());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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
            Log.i("Error", "Failure");
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
    public static ArrayList<UserRequest> getOwnActiveRequests(User user, Context context) {
        /**
         * For use in ride mode only
         * Gets all requests created by the user (active)
         */
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();
        // (get requests accepted by the curr user)
        if (UserController.checkMode() == DRIVER) {
            ElasticsearchRequestController.GetActiveDriverRequests searchController = new ElasticsearchRequestController.GetActiveDriverRequests();

            try {
                userRequests = searchController.execute(user.getId()).get();

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        else {

            if(!FileController.isNetworkAvailable(context)) {
                userRequests = FileController.loadFromFile(riderRequests, context);
            } else {
                ElasticsearchRequestController.GetActiveRiderRequests activeController = new ElasticsearchRequestController.GetActiveRiderRequests();
                try{
                    userRequests = activeController.execute(user.getId()).get();
                    FileController.saveInFile(userRequests, riderRequests, context);
                } catch (Exception e){
                    e.printStackTrace();
                }
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
    public static ArrayList<UserRequest> getAcceptedByDrivers(User user, Context context) {
        /**
         * For use in ride mode only. Gets the requests that user has created and
         * are currently accepted by at least one driver.
         * Excludes completed requests.
         */
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();
        ArrayList<UserRequest> riderRequests = getOwnActiveRequests(user, context);
        for (UserRequest request: riderRequests) {
            if (request.getRequestStatus() == RequestStatus.ACCEPTED) {
                userRequests.add(request);
            }
        }

        return userRequests;
    }

    public static ArrayList<UserRequest> getOfflineAcceptances() {
       if(CommandStack.getAcceptedCommands() == null){
            return new ArrayList<>();
        }
        return CommandStack.getAcceptedCommands();
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
    public static ArrayList<UserRequest> getAcceptedByUser(User user, Context context) {
        /**
         * For use in driver mode only. Gets the requests that user has currently
         * accepted, excluding completed requests.
         */
        // drivers only
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest> ();
        // check network
        if(!FileController.isNetworkAvailable(context)) {
           userRequests = FileController.loadFromFile(acceptedByDriver, context);
        } else {
            ElasticsearchRequestController.GetAcceptedByMe searchController =
                    new ElasticsearchRequestController.GetAcceptedByMe();

            try {
                userRequests = searchController.execute(user.getId()).get();
                FileController.saveInFile(userRequests, acceptedByDriver, context);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
    public static ArrayList<UserRequest> getCompletedRequests(User user, Mode mode, Context con) {
        /**
         * Gets the completed requests BY a driver if mode == Mode.DRIVER
         * Gets the completed requests a rider received if mode == Mode.RIDER
         */
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest> ();
        ArrayList<UserRequest> result = new ArrayList<UserRequest> ();


        if (mode == Mode.DRIVER) {
            if(FileController.isNetworkAvailable(con)) {
                ElasticsearchRequestController.GetPastDriverRequests getPastDriverRequests =
                        new ElasticsearchRequestController.GetPastDriverRequests();
                try{
                    userRequests = getPastDriverRequests.execute(user.getId()).get();
                    FileController.saveInFile(userRequests, completedByDriver, con);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                userRequests = FileController.loadFromFile(completedByDriver, con);
            }

        }else{
            if(FileController.isNetworkAvailable(con)) {
                ElasticsearchRequestController.GetPastRiderRequests getPastRiderRequests =
                        new ElasticsearchRequestController.GetPastRiderRequests();

                try{
                    userRequests = getPastRiderRequests.execute(user.getId()).get();
                    FileController.saveInFile(userRequests, completedByDriver, con);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                userRequests = FileController.loadFromFile(completedByDriver, con);
            }

        }
        for (UserRequest request: userRequests) {
            if (request.isPaymentRecived()) {
                result.add(request);
            }
        }
        return result;
    }

    /**
     * Mark a given request as paid for
     * @param request that has been paid for
     */

    public static void payRequest(UserRequest request) {
        request.setPaymentReceived(true);
        ElasticsearchRequestController.UpdateClosedRequestObject searchController =
                new ElasticsearchRequestController.UpdateClosedRequestObject();
        try {
            Boolean result = searchController.execute(request).get();
        } catch (Exception e) {
            Log.e("markPaid", e.toString());
        }

    }

    /**
     * Get a users requests that have been completed but not yet paid for
     *
     * @param user the current user
     * @param mode the mode that the user is in
     * @return
     */

    public static ArrayList<UserRequest> getAwaitingPaymentRequests(User user, Mode mode) {
        /**
         * Gets the completed requests BY a driver if mode == Mode.DRIVER
         * Gets the completed requests a rider received if mode == Mode.RIDER
         */
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest> ();
        ArrayList<UserRequest> result = new ArrayList<UserRequest>();


        if (mode == Mode.DRIVER) {
            ElasticsearchRequestController.GetAwaitingPaymentDriverRequests getAwaitingPaymentDriverRequests =
                    new ElasticsearchRequestController.GetAwaitingPaymentDriverRequests();
            try{
                userRequests = getAwaitingPaymentDriverRequests.execute(user.getId()).get();
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            ElasticsearchRequestController.GetAwaitingPaymentRiderRequests getAwaitingPaymentRiderRequests =
                    new ElasticsearchRequestController.GetAwaitingPaymentRiderRequests();

            try{
                userRequests = getAwaitingPaymentRiderRequests.execute(user.getId()).get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for (UserRequest request: userRequests) {
            if (!request.isPaymentRecived()) {
                result.add(request);
            }
        }
        return result;
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
        ArrayList<String> users;
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
    private static ArrayList<User> getUserInfo(ArrayList<String> users){
        ArrayList<User> actualUserObjects = new ArrayList<>();
        for(String userId: users){
            ElasticsearchUserController.RetrieveUserInfoFromId impl =
                    new ElasticsearchUserController.RetrieveUserInfoFromId();
            User actualUser;
            try {
                actualUser = impl.execute(userId).get();
                actualUserObjects.add(actualUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return actualUserObjects;
    }    /**
     * Gets the price per kilometer involved in a request
     *
     * @param request the UserRequest obj that we are getting price/km for
     * @return double of price per km ($/km)
     */
    public static double getPricePerKM(UserRequest request) {
        return (request.getFare().doubleValue() / request.getDistance());
    }

    public static ArrayList<UserRequest> getOfflineRequests() {
        if(CommandStack.getAddCommands() == null){
            return new ArrayList<>();
        }
        return CommandStack.getAddCommands();
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
                temp = searchController.execute(user.getId()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            // driver mode
            ElasticsearchRequestController.GetInPrgressRequests searchController =
                    new ElasticsearchRequestController.GetInPrgressRequests();
            try {
                temp = searchController.execute(user.getId()).get();
                // work around for elasticsearchrequestcontroller
                ArrayList<UserRequest> temp_copy = new ArrayList<UserRequest>();
                temp_copy.addAll(temp);
                for (UserRequest request: temp_copy) {
                    if (!request.getConfirmedDriverID().equals(user.getId())) {
                        temp.remove(request);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public static RequestStatus getRequestStatus(UserRequest request) {
        return request.getRequestStatus();
    }

    /**
     * Move a request from open to inProgress
     *
     * @param ur the UserRequest to be movved
     */
    @Deprecated
    public static void moveToInProgress(UserRequest ur){
        ElasticsearchRequestController.MoveToInProgresseRequest moveToInProgresseRequest = new ElasticsearchRequestController.MoveToInProgresseRequest();
        moveToInProgresseRequest.execute(ur);

    }

    /**
     * Move a request from inProgress to closed
     *
     * @param ur the UserRequest to be moved
     */
    @Deprecated
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
     * @param request the request to be deleted
     * @return true if successful, false otherwise
     */
    public static Boolean deleteRequest(UserRequest request) {
        deletedRequest = request;
        ElasticsearchRequestController.DeleteRiderRequests deleteRequestsById =
                new ElasticsearchRequestController.DeleteRiderRequests();
        try {
            deleteRequestsById.execute(request.getId());

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Gets the request that has been delete
     *
     * @return te request in question
     */
    public static UserRequest getDeletedRequest() {
        return deletedRequest;
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

    /**
     * Fetch requests whose start Locations match a given keyword
     *
     * @param keywords keywords to search by
     * @return ArrayList of UserRequest
     */
    public static ArrayList<UserRequest> queryByKeywordStartLocation(String keywords){
        ElasticsearchRequestController.GetRequestsByStartLocationKeyword getRequestsByStartLocationKeyword =
                new ElasticsearchRequestController.GetRequestsByStartLocationKeyword();
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();

        try{
            userRequests = getRequestsByStartLocationKeyword.execute(keywords).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return userRequests;
    }

    /**
     * Fetch requests whose end Locations match a given keyword
     *
     * @param keywords keywords to search by
     * @return ArrayList of UserRequest
     */
    public static ArrayList<UserRequest> queryByKeywordEndLocation(String keywords){
        ElasticsearchRequestController.GetRequestsByEndLocationKeyword getRequestsByEndLocationKeyword =
                new ElasticsearchRequestController.GetRequestsByEndLocationKeyword();
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();

        try{
            userRequests = getRequestsByEndLocationKeyword.execute(keywords).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return userRequests;
    }


    @Deprecated
    public static ArrayList<UserRequest> queryByKeywordUserName(String keywords){
        ElasticsearchRequestController.GetRequestsByUserNameKeyword getRequestsByUserNameKeyword =
                new ElasticsearchRequestController.GetRequestsByUserNameKeyword();
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();

        try{
            userRequests = getRequestsByUserNameKeyword.execute(keywords).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return userRequests;

    }

    /**
     * Fetch requests whose descriptions match a given keyword
     *
     * @param keywords keywords to search by
     * @return ArrayList of UserRequest
     */
    public static ArrayList<UserRequest> queryByKeywordDescription(String keywords){
        ElasticsearchRequestController.GetRequestsByDescriptionKeyword getRequestsByDescriptionKeyword=
                new ElasticsearchRequestController.GetRequestsByDescriptionKeyword();
        ArrayList<UserRequest> userRequests = new ArrayList<UserRequest>();

        try{
            userRequests = getRequestsByDescriptionKeyword.execute(keywords).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return userRequests;


    }
    public static void updateDriverRating(String driverId, Double newRating){
        ElasticsearchUserController.AddToDriverRating addToDriverRating =
                new ElasticsearchUserController.AddToDriverRating();
        addToDriverRating.execute(driverId,newRating.toString());

    }


    /**
     * Get location names from Lat Lon coordinates
     *
     * @param start LatLng start location
     * @param end LatLng end location
     * @param context current context
     * @return
     */

    public static ArrayList<String> searchLocationName(LatLng start, LatLng end, Context context) {

        String startLocationName ="";
        String endLocationName = "";

        //This is added so that we can search by a location name
        try {
            Geocoder geoCoder = new Geocoder(context);
            List<Address> startString = geoCoder.getFromLocation(start.latitude, end.longitude, 1);
            List<Address> endString = geoCoder.getFromLocation(end.latitude, end.longitude, 1);
            if (!startString.isEmpty()) {
                startLocationName = startString.get(0).getAddressLine(0);
            }
            if (!endString.isEmpty()) {
                endLocationName = endString.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            Log.i("Error", "Unable to decode address");
            e.printStackTrace();
        }
        ArrayList<String> locationList = new ArrayList<>();
        locationList.add(startLocationName);
        locationList.add(endLocationName);

        return locationList;

    }

    /**
     * Uses an offline request that has characteristic
     * @param request request that has been set offline and is ready to be converted
     * @param context context for the geocoder to use
     * @return a valid request able to pushed to elastic search server
     *
     * Adapter Pattern
     */
    public static UserRequest convertOfflineRequestToOnlineRequest(UserRequest request, Context context){

        //Set the start and end location of it
        try {
            Geocoder geoCoder = new Geocoder(context);
            List<Address> start = geoCoder.getFromLocation(request.getStartLocation().latitude, request.getStartLocation().longitude, 1);
            List<Address> end = geoCoder.getFromLocation(request.getEndLocation().latitude, request.getEndLocation().longitude, 1);
            if(!start.isEmpty()){
                request.setStartLocationName(start.get(0).getAddressLine(0));
            }
            if(!end.isEmpty()) {
                request.setEndLocationName(end.get(0).getAddressLine(0));
            }
        } catch (Exception e) {
            Log.i("Error", "Unable to decode address");
            e.printStackTrace();
        }
        DistanceCalculator calc = new DistanceCalculator();
        calc.execute(request.getStartLocation(), request.getEndLocation());

        String dist = "";
        try{
            dist = calc.get();
        }catch(Exception e){
            e.printStackTrace();
        }
        Scanner sc = new Scanner(dist);
        double doubleDistance = sc.nextDouble();

        request.setDistance(doubleDistance);

        return request;
    }
}

