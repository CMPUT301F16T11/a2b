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

        User user = new User("someone","asgasd@asgs.ca","999");
        user.setId("asvearve435");

        UserRequest mockReq = new UserRequest(new LatLng(53.5232,113.5263),new LatLng(53.525,113.521),10,user);

        ElasticsearchRequestController.AddOpenRequestTask addOpenRequestTask = new ElasticsearchRequestController.AddOpenRequestTask();
        addOpenRequestTask.execute(mockReq);


        // Testing Login Activity atm
        Intent intent = new Intent(this, LoginActivity.class);

        //Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

}
