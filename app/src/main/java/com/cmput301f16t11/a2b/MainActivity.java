package com.cmput301f16t11.a2b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

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
//
//        UserRequest mockReq = new UserRequest(new LatLng(53.5232,113.5263),new LatLng(53.525,113.521),10,user);
//
//        ElasticsearchRequestController.AddOpenRequestTask addOpenRequestTask = new ElasticsearchRequestController.AddOpenRequestTask();
//        ElasticsearchRequestController.MoveToInprogresseRequest moveToInprogresseRequest = new ElasticsearchRequestController.MoveToInprogresseRequest();
//        ElasticsearchRequestController.MoveToClosedRequest moveToClosedRequest = new ElasticsearchRequestController.MoveToClosedRequest();
//
//        try {
//            addOpenRequestTask.execute(mockReq).get();
//            moveToInprogresseRequest.execute(mockReq).get();
//            moveToClosedRequest.execute(mockReq).get();
//
//        }catch(Exception e){
//
//        }

        // Testing Login Activity atm
        Intent intent = new Intent(this, LoginActivity.class);

        //Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

}
