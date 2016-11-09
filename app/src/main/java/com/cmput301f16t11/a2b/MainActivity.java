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
////        user.setId("asvearve435");
////
//        //UserRequest mockReq1 = new UserRequest(new LatLng(113.526230,53.523201),new LatLng(99.999999,88.888888),10,user);
//        //UserRequest mockReq2 = new UserRequest(new LatLng(113.526130,53.523200),new LatLng(99.999999,88.888888),10,user);
////
//       ElasticsearchRequestController.AddOpenRequestTask addOpenRequestTask = new ElasticsearchRequestController.AddOpenRequestTask();
//        ElasticsearchRequestController.GetNearbyRequestsGeoFilter getNearbyRequestsGeoFilter = new ElasticsearchRequestController.GetNearbyRequestsGeoFilter();
////        ElasticsearchRequestController.MoveToClosedRequest moveToClosedRequest = new ElasticsearchRequestController.MoveToClosedRequest();
////
//       try {
//            //addOpenRequestTask.execute(mockReq1).get();
//           //addOpenRequestTask.execute(mockReq2).get();
//           getNearbyRequestsGeoFilter.execute(100.0,53.523201,113.526230).get();
////            moveToClosedRequest.execute(mockReq).get();
////
//        }catch(Exception e){
////
//        }

        // Testing Login Activity atm
        Intent intent = new Intent(this, LoginActivity.class);

        //Intent intent = new Intent(this, RequestListActivity.class);
        startActivity(intent);
    }

}
