package com.cmput301f16t11.a2b;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Main activity to load activities for testing purposes.
 *
 * Will be depreciated in final product
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TEMPORARY CODE - LAUNCHES the location activity for testing
        //Intent intent = new Intent(this, RiderLocationActivity.class);
        // startActivity(intent)

//        User user = UserController.getUserFromName("brian");
//
//        UserRequest mockReq1 = new UserRequest(new LatLng(37.4219983,-122.084),new LatLng(99.999999,88.888888),10,user.getId());
//        UserRequest mockReq2 = new UserRequest(new LatLng(37.4219983,-122.084),new LatLng(99.999999,88.888888),10,user.getId());
//        UserRequest mockReq3 = new UserRequest(new LatLng(37.4219983,-122.084),new LatLng(99.999999,88.888888),10,user.getId());
//        UserRequest mockReq4 = new UserRequest(new LatLng(37.4219983,-122.084),new LatLng(99.999999,88.888888),10,user.getId());
//        UserRequest mockReq5 = new UserRequest(new LatLng(37.4219983,-122.084),new LatLng(99.999999,88.888888),10,user.getId());
//        UserRequest mockReq6 = new UserRequest(new LatLng(37.4219983,-122.084),new LatLng(99.999999,88.888888),10,user.getId());
//
//        ArrayList<UserRequest> reqs = new ArrayList<UserRequest>();
//        reqs.add(mockReq1);
//        reqs.add(mockReq2);
//        reqs.add(mockReq3);
//        reqs.add(mockReq4);
//        reqs.add(mockReq5);
//        reqs.add(mockReq6);
//
//        RequestController.addBatchOpenRequests(reqs);




            // Testing Login Activity atm
        Intent intent = new Intent(this, LoginActivity.class);

        //Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
        finish();
    }

}
