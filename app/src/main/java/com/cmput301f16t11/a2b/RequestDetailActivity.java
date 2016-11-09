package com.cmput301f16t11.a2b;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RequestDetailActivity extends AppCompatActivity {
    /**
     * This work, "RequestDetailActivity," contains a derivative
     * "Android - How to create clickable listview?" by "Delpes," a stackoverflow user,
     * used under CC-BY-SA by CMPUT301F16T11.
     * (Available here:
     * http://stackoverflow.com/questions/13281197/android-how-to-create-clickable-listview)
     */
    // TODO: Remove confirm button
    // TODO: Allow option to confirm on accepted Driver click if appliactable
            // potentially a dialog with a complete button is applicable
            // if not a view user button
    private UserRequest request;
    private ListView driverList; // TODO: populate this list
    ArrayList<User> acceptgitedDrivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
    }

    @Override
    public void onResume() {
        super.onResume();
        Gson gson = new Gson();
        Intent intent = getIntent();
        request = gson.fromJson(intent.getStringExtra("request"), UserRequest.class);
        populateAcceptedDriversList();
        populateFields();
        setButtons();
    }

    public void populateAcceptedDriversList() {
        acceptedDrivers = request.getAcceptedDrivers();
        driverList = (ListView) findViewById(R.id.accepted_list);
        driverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                Intent intent = new Intent(RequestDetailActivity.this, ProfileActivity.class);
                intent.putExtra("username", acceptedDrivers.get(position).toString());
                startActivity(intent);
            }
        });
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, acceptedDrivers);

    }

    public void populateFields() {
        TextView driverName = (TextView) findViewById(R.id.request_detail_driver);
        driverName.setText(request.getConfirmedDriver().toString());
        driverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDetailActivity.this, ProfileActivity.class);
                intent.putExtra("username", request.getConfirmedDriver().toString());
                startActivity(intent);
            }
        });

        TextView riderName = (TextView) findViewById(R.id.request_detail_rider);
        riderName.setText(request.getRider().toString());
        riderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDetailActivity.this, ProfileActivity.class);
                intent.putExtra("username", request.getRider().toString());
                startActivity(intent);
            }
        });

        TextView startLocation = (TextView) findViewById(R.id.request_detail_pickup);
        String location_string = getLocationString(request.getStartLocation());
        startLocation.setText(location_string);
        startLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO draw on map
            }
        });

        TextView endLocation = (TextView) findViewById(R.id.request_detail_dropoff);
        location_string = getLocationString(request.getEndLocation());
        endLocation.setText(location_string);
        endLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO draw on map
            }
        });

        TextView fare = (TextView) findViewById(R.id.request_detail_fare);
        fare.setText("$" + request.getFare().toString());

    }

    public String getLocationString(LatLng location) {

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (!addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            Log.i("Error", "Failed to find address of location");
            e.printStackTrace();
        }
        return location.toString();
    }

    public void setButtons() {
        Button deleteButton = (Button) findViewById(R.id.request_detail_delete);
        Button acceptButton = (Button) findViewById(R.id.request_detail_accept);
        Button confirmButton = (Button) findViewById(R.id.request_confirm);
        Button completeButton = (Button) findViewById(R.id.request_complete);
        // confirm and delete
        if (UserController.checkMode() ==
                Mode.RIDER && UserController.getUser().equals(request.getRider())) {
            deleteButton.setEnabled(true);
            if (request.hasConfirmedRider()) {
                confirmButton.setEnabled(true);
            }
            else {
                confirmButton.setEnabled(false);
            }
        }
        else {
            deleteButton.setEnabled(false);
        }
        // accept
        if (UserController.checkMode() == Mode.DRIVER &&
                !request.getAcceptedDrivers().contains(UserController.getUser())) {
                acceptButton.setEnabled(true);
        }
        else {
            acceptButton.setEnabled(false);
        }


    }

}
