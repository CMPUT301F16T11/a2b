package com.cmput301f16t11.a2b;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;

/**
 * This class is a background service that get runs when the user as a rider creates an active request. It
 * continuously checks the server for that specific request and send a notification if there is an added
 * driver that accepts that ride.
 * Will be terminated when the user
 */

public class RiderNotificationService extends IntentService {
    private UserRequest request;

    //Elastic search stuff
    private static JestDroidClient client;
    private static String index = "f16t11";

    private static String openRequest = "openRequest";
    private static RiderNotificationService self;

    public RiderNotificationService(UserRequest request){
        super("intent service");
        this.request = request;
        self = this;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        verifySettings();
        ArrayList<User> acceptedDrivers = request.getAcceptedDrivers();
        while(true) {

            ArrayList<User> serverAcceptedDrivers = getRequestFromId(request.getId());
            ArrayList<User> differentUser = findDifferenceRequests(acceptedDrivers, serverAcceptedDrivers);
            sendNotificationOfAcceptedDriver(differentUser);

           try{
               Thread.sleep(10000);
           }catch(InterruptedException e){
               e.printStackTrace();
           }
       }
    }

    public static Intent createIntentStartNotificationService(Context context) {

        Intent intent = new Intent(context, RiderNotificationService.class);
        return intent;
    }

    public static void endService(){

        if(self != null){
            self.stopSelf();
        }
    }

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

    private ArrayList<User> findDifferenceRequests(ArrayList<User> userOriginal, ArrayList<User> userServer){

        userServer.removeAll(userOriginal);
        return userServer;
    }

    private ArrayList<User> getRequestFromId(String requestId){
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
                return null;
            }
        } catch (Exception e) {
            Log.i("Error", "Failed to communicate with elasticsearch server");
            e.printStackTrace();
            return null;
        }

        return userRequest.getAcceptedDrivers();
    }

    private  void sendNotificationOfAcceptedDriver(ArrayList<User> addedUsers){
        String notification = "";
        for(User user: addedUsers){
            notification = notification + user.getName() +", ";
        }
        notification = notification + "has Accepted request" + request.getId();

        Notification noti = new Notification.Builder(this)
                .setContentTitle(notification)
                .setSmallIcon(R.drawable.common_plus_signin_btn_icon_dark)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, noti);
    }
}
