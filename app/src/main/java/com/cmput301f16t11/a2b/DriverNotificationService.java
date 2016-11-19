package com.cmput301f16t11.a2b;

import android.app.Activity;
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
import java.util.Iterator;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;

/**
 * This notification handler sets up when a driver accepts a rider ride. It then waits on the rider
 * to confirm and update the request with confirmed driver. It periodically checks the server for differences.
 * Sends a notification if the rider confirms or rejects a driver acceptance
 */

public class DriverNotificationService extends IntentService {
    private User driver;
    private ArrayList<UserRequest> requests = new ArrayList<>();

    //Elastic search stuff
    private static JestDroidClient client;
    private static String index = "f16t11";

    private static String type = "inProgress";
    private static DriverNotificationService self;

    public DriverNotificationService(){
        super("intent service");
        self = this;
        driver = UserController.getUser();
    }

    public DriverNotificationService(ArrayList<UserRequest> requests) {
        super("intent service");
        this.requests = requests;
        driver = UserController.getUser();
        self = this;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        verifySettings();
        while(true) {
            synchronized (requests) {
                // Iterator idea taken to avoid exceptions while removing inside for loop
                // Taken from the link below on November 18, 2016
                // http://stackoverflow.com/questions/223918/iterating-through-a-collection-avoiding-concurrentmodificationexception-when-re
                for (Iterator<UserRequest> iterator = requests.iterator(); iterator.hasNext(); ) {
                    UserRequest request = iterator.next();
                    UserRequest serverRequest = getInProgressRequest(request.getId());
                    if (serverRequest != null) {
                        //If the driver is accepted notify him also notify the drivers that are not chose
                        if (serverRequest.getConfirmedDriver().equals(driver.getId())) {
                            sendNotificationOfRiderConfirmed(serverRequest);
                        } else {
                            sendNotificationOfRiderRejected(serverRequest);
                        }
                        iterator.remove();
                    }
                }

                // If there are no more requests stop the service
                if (requests.size() == 0) {
                    stopSelf();
                }
            }

            // Wait 3s before trying again
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
       }
    }

    /**
     * This is static call that allows the user to retrieve a valid intent to start a service
     *
     * @param context the context of which the intent should be created form
     */
    public static Intent createIntentDriverNotificationService(Context context) {
        Intent intent = new Intent(context, DriverNotificationService.class);
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
     * @param requestId id of the desired request
     * @return the request object corresponding to the requestId argument from inProgress
     */
    private UserRequest getInProgressRequest(String requestId){
        verifySettings();
        Get get = new Get.Builder(index, requestId).type(type).build();

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
     *
     * @param request the request that a notification will be sent regarding
     */
    private void sendNotificationOfRiderConfirmed(UserRequest request){
        String notification = request.getRider()+ " has accepted your ride for request " + request.getId();

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
     *
     * @param request the request that has been rejected
     */
    private  void sendNotificationOfRiderRejected(UserRequest request){
        String notification = request.getRider()+ " has taken another ride for request " + request.getId();

        Notification noti = new Notification.Builder(this)
                .setContentTitle(notification)
                .setSmallIcon(R.drawable.common_plus_signin_btn_icon_dark)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

    public void addRequest(UserRequest req) {
        synchronized (requests) {
            requests.add(req);
        }
    }

    public static Boolean isStarted() {
        if (self==null) {
            return false;
        }
        return true;
    }

    public static void serviceHandler(UserRequest req, Activity activity) {
        if (!DriverNotificationService.isStarted()) {
            Intent intent = createIntentDriverNotificationService(activity);
            activity.startService(intent);
        }

        DriverNotificationService.self.addRequest(req);
    }

}
