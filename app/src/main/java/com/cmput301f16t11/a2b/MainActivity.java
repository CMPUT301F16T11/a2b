package com.cmput301f16t11.a2b;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TEMPORARY CODE - LAUNCHES the location activity for testing
        //Intent intent = new Intent(this, RiderLocationActivity.class);
        // startActivity(intent)

//        User user = new User("someone2","asgasd@asgs.ca","666");
//        user.setId("asvearve435");
        //UserRequest mockReq1 = new UserRequest(new LatLng(37.4219983,-122.084),new LatLng(99.999999,88.888888),10,user);
//        ElasticsearchRequestController.AddOpenRequestTask addOpenRequestTask = new ElasticsearchRequestController.AddOpenRequestTask();
//
//        try {
//            addOpenRequestTask.execute(mockReq1).get();
//            //getRequest.execute(mockReq1.getId()).get();
//
//        }catch(Exception e){
//
//        }


//
       //ArrayList<UserRequest> ar =  RequestController.getNearbyRequestsGeoFilter();

        //UserRequest mockReq1 = RequestController.getOpenRequestById("AVhPSldAd-2jFxhHx-yR");



            // Testing Login Activity atm
        Intent intent = new Intent(this, LoginActivity.class);

        //Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

}
