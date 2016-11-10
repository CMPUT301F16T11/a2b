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
 * This notification handler sets up when a driver accepts a rider ride. It then waits on the rider
 * to confirm and update the request with confirmed driver. It periodically checks the server for differences.
 * Sends a notification if the rider confirms or rejects a driver acceptance
 */

public class DriverNotificationService extends IntentService {
    private User driver;
    private UserRequest request;

    //Elastic search stuff
    private static JestDroidClient client;
    private static String index = "f16t11";

    private static String inProgress = "inProgress";
    private static DriverNotificationService self;

    public DriverNotificationService(User driver, UserRequest request){
        super("intent service");
        this.driver = driver;
        this.request = request;

        self = this;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        verifySettings();
        while(true) {

            UserRequest serverRequest = getInProgressRequest(request.getId());
            if(serverRequest != null){

                //If the driver is accepted notify him also notify the drivers that are not chose
                if(serverRequest.getConfirmedDriver().getName().equals(driver.getName())){
                    sendNotificationOfRiderConfirmed(serverRequest);
                }
                else{
                    sendNotificationOfRiderRejected(serverRequest);
                }
                stopSelf();
            }

            try{
                Thread.sleep(10000);
            }catch(InterruptedException e){
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
     * This is a method from the ElasticsearchRequestController that finds thein progress request from
     * the server is there is any.
     * @param requestId
     * @return
     */
    private UserRequest getInProgressRequest(String requestId){
        verifySettings();
        Get get = new Get.Builder(index, requestId).type(inProgress).build();

        UserRequest userRequest;
        try {
            JestResult result = client.execute(get);
            if (result.isSucceeded()) {
                userRequest = result.getSourceAsObject(UserRequest.class);
                return userRequest;
            }
            else{
                return null;
            }
        } catch (Exception e) {
            Log.i("Error", "Failed to communicate with elasticsearch server");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Sends a notification to the phone that the rider has accepted the driver ride.
     * @param request
     */
    private  void sendNotificationOfRiderConfirmed(UserRequest request){
        String notification = request.getRider()+ "has confirmed your request."+ request.getId();

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

    /**
     * Sends a notification to the current phone that the rider had rejected the ride the driver
     * offered.
     * @param request
     */
    private  void sendNotificationOfRiderRejected(UserRequest request){
        String notification = request.getRider()+ "has reject your request.";

        notification = notification + "has Accepted request" + request.getId();

        Notification noti = new Notification.Builder(this)
                .setContentTitle(notification)
                .setSmallIcon(R.drawable.common_plus_signin_btn_icon_dark)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

        //TODO: re-enable the driver to be able to accept requests
    }
}
