package com.cmput301f16t11.a2b;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RequestListActivity extends AppCompatActivity {
    /**
     * This work, "MainHabitActivity," contains a derivative
     * "Android - How to create clickable listview?" by "Delpes," a stackoverflow user,
     * used under CC-BY-SA by Joey-Michael Fallone.
     * (Available here:
     * http://stackoverflow.com/questions/13281197/android-how-to-create-clickable-listview)
     */
    private ArrayList<UserRequest> requests;
    private ArrayAdapter<UserRequest> adapter;
    private ListView listView;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerChoices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        this.requests = new ArrayList<UserRequest>();
    }

    @Override
    public void onResume() {
        super.onResume();
        // listView Stuff
        listView = (ListView) findViewById(R.id.requestList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                //TODO: some code here that responds to clicking on request
                // perhaps a dialog?
                // set view colour
                v.setBackgroundColor(Color.rgb(127, 215, 245));
                Intent intent = new Intent(v.getContext(), RequestDetailActivity.class);
                Gson gson = new Gson();
                String request = gson.toJson(requests.get(position));
                intent.putExtra("request", request);
                startActivity(intent);
            }
        });
        adapter = new ShadedListAdapter<UserRequest>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, this.requests);
        listView.setAdapter(adapter);
        spinner = (Spinner) findViewById(R.id.requestSpinner);
        adapter.notifyDataSetChanged();

        Mode mode = UserController.checkMode();
        // spinner stuff
        if (UserController.checkMode() == Mode.DRIVER) {
            String[] choices = getResources().getStringArray(R.array.requestTypesDriverArray);
            spinnerChoices = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, choices);
        } else {
            // rider
            String[] choices = getResources().getStringArray(R.array.requestTypesRiderArray);
            spinnerChoices = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, choices);
        }

        spinner.setAdapter(spinnerChoices);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Driver Mode
                    if (UserController.checkMode() == Mode.DRIVER) {
                        requests.addAll(RequestController.getNearbyRequests());
                    }
                    else {
                        // rider mode!
                        // my requests
                        requests.clear();
                        requests.addAll(
                                RequestController.getOwnActiveRequests(UserController.getUser()));

                    }
                    adapter.notifyDataSetChanged();
                } else if (position == 1) {
                    // Accepted by Me (for drivers: by ME, for riders: by at least 1 driver
                    if (UserController.checkMode().equals(Mode.DRIVER)) {
                        requests.clear();
                        requests.addAll(RequestController.getAcceptedByUser(UserController.getUser()));
                    } else {
                        // users
                        requests.clear();
                        requests.addAll(RequestController.getAcceptedByDrivers(UserController.getUser()));
                    }
                    adapter.notifyDataSetChanged();
//                    populateRequestList();
                } else if (position == 2) {
                    // confirmed requests
                    // if rider this will be requests the USER has confirmed after a driver has
                    // accepted them
                    // if driver, this will be requests ANOTHER USER has confirmed as a rider
                    // after accepted by the curr user
                    requests.clear();
                    requests.addAll(RequestController.getConfirmedByRiders(UserController.getUser(),
                            UserController.checkMode()));
                    adapter.notifyDataSetChanged();
//                    populateRequestList();
                } else if (position == 3) {
                    // completed requests
                    // if driver, display completed as driver
                    // if rider, display completed as rider
                    requests.clear();
                    requests.addAll(RequestController.getCompletedRequests(UserController.getUser(),
                            UserController.checkMode()));
                    adapter.notifyDataSetChanged();
//                    populateRequestList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg) {
                // do nothing
            }
        });
    }
}
