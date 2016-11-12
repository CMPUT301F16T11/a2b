package com.cmput301f16t11.a2b;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.Map;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;

/**
 * This class is a background service that get runs when the user as a rider creates an active request. It
 * continuously checks the server for that specific request and send a notification if there is an added
 * driver that accepts that ride.
 * Will be terminated when the user
 */
public class RiderNotificationService extends IntentService {

    //Elastic search stuff
    private static JestDroidClient client;
    private static String index = "f16t11";

    private static String openRequest = "openRequest";
    private static String user = "user";
    private static ArrayList<UserRequest> requestMonitoring = new ArrayList<>();
    private static RiderNotificationService self;

    public RiderNotificationService(){
        super("Rider Notification service");
        this.self = this;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        verifySettings();
        while(true) {
            //Added to avoid editing this list while the background thread looks at it
            synchronized (requestMonitoring) {
                for (UserRequest request : requestMonitoring) {
                    ArrayList<User> acceptedDrivers = request.getAcceptedDrivers();
                    ArrayList<User> serverAcceptedDrivers = getAcceptedDriversFromId(request.getId());
                    ArrayList<User> differentUser = findDifferenceRequests(acceptedDrivers, serverAcceptedDrivers);

                    //If there is an accepted user
                    if (differentUser.size() != 0) {
                        sendNotificationOfAcceptedDriver(serverAcceptedDrivers, request.getId());

                        for (User user : differentUser) {
                            addDriverToMonitor(request, user);
                        }
                    }
                }
            }

            //We need this here so we arent constantly hogging resource and communication with server
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * This is static call that allows the user to retrieve a valid intent to start a service
     */
    public static Intent createIntentStartNotificationService(Context context) {

        Intent intent = new Intent(context, RiderNotificationService.class);
        return intent;
    }

    /**
     * determines if a service is started so there is only one service at any point
     * @return
     */
    public static Boolean isRecieveServiceStarted(){

        if(self == null){
            return false;
        }
        return true;
    }

    public void addDriverToMonitor(UserRequest request, User user){
        int index = requestMonitoring.indexOf(request);

        synchronized (requestMonitoring) {
            requestMonitoring.get(index).addAcceptedDriver(user);
        }
    }

    public static void addRequestToBeNotified(UserRequest request){

       synchronized (requestMonitoring){
           requestMonitoring.add(request);
       }
    }

    /**
     * So we will no longer send request about that request id
     */
    public static void endNotification(String id){

        synchronized (requestMonitoring) {
            requestMonitoring.remove(id);
        }

        if (requestMonitoring.size() == 0) {
            self.stopSelf();
        }
    }

    /**
     * This is from the ElasticRequestController it sets up the Droid client object
     */
    private void verifySettings() {
        // Initialize client if necessary
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://35.162.68.100:9200");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

    /**
     * This method return a collection of all the user that are in the server users but not in the original user
     * @param userOriginal
     * @param userServer
     * @return list of accepted drivers for that request
     */
    private ArrayList<User> findDifferenceRequests(ArrayList<User> userOriginal, ArrayList<User> userServer){
        if(userServer == null){
            return new ArrayList<>();
        }

        for(User userOr : userOriginal){
            for(int i = 0; i< userServer.size();){
                if(userServer.get(i).getId().equals(userOr.getId())){
                    userServer.remove(i);
                }
                else{
                    i++;
                }
            }
        }

        return userServer;
    }

    /**
     * This is a method from the ElasticsearchRequestController as well that get the accepted drivers from
     * a specfiic request id.
     * @param requestId
     * @return
     */
    private ArrayList<User> getAcceptedDriversFromId(String requestId){
        verifySettings();

        Get get = new Get.Builder(index, requestId).type(openRequest).build();
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

    /**
     * This send a notification that all the addedUsers have accepted that individual rider's request
     * @param addedUsers
     */
    private  void sendNotificationOfAcceptedDriver(ArrayList<User> addedUsers, String requestId){
        String notification = "";
        for(User user: addedUsers){
            String name = getUserName(user.getId());
            notification = notification + name +", ";
        }
        notification = notification + "has Accepted request " + requestId;

        Notification noti = new Notification.Builder(this)
                .setContentTitle(notification)
                .setSmallIcon(R.drawable.ic_notification_a2b)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, noti);
    }

    private String getUserName(String usernameId){
        Get get = new Get.Builder(index, usernameId).type(user).build();
        User user;
        try {
            JestResult result = client.execute(get);
            if (result.isSucceeded()) {
                user = result.getSourceAsObject(User.class);
                return user.getName();
            }
            else{
                Log.i("Error", "Failed to find any accepted requests");
                return "<username not found>";
            }
        } catch (Exception e) {
            Log.i("Error", "Failed to communicate with elasticsearch server");
            e.printStackTrace();
            return "<username not found>";
        }
    }
}
