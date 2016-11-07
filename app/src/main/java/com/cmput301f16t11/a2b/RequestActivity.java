package com.cmput301f16t11.a2b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
    }
    // TEMPORARY POPULATION OF RANDOM REQUESTS!!!
    private ArrayList<Request> tempFakeRequestList() {
        ArrayList<Request> returnValue = new ArrayList<Request>();
        returnValue.add(new Request(new LatLng(53.5443890, -113.4909270),
                        new LatLng(54.07777, -113.50192), new User("test", "test@email.com")));
        returnValue.add(new Request(new LatLng(54.07777, -113.50192),
                        new LatLng(53.5443890, -113.4909270), new User("test", "test@email.com")));
        returnValue.add(new Request(new LatLng(54.07777, -113.50192),
                new LatLng(53.5443890, -113.4909270), new User("test2", "test2@email.com")));
        return returnValue;
    }
}
