package com.cmput301f16t11.a2b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.gms.maps.model.LatLng;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        spinner = (Spinner) findViewById(R.id.requestSpinner);
        this.requests = RequestController.tempFakeRequestList();
        if (UserController.checkMode().equals(Mode.DRIVER)) {
            String [] choices = getResources().getStringArray(R.array.requestTypesDriverArray);
            spinnerChoices = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_dropdown_item, choices);
        }
        else {
            // rider
            String [] choices = getResources().getStringArray(R.array.requestTypesRiderArray);
            spinnerChoices = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_dropdown_item, choices);
        }
        spinner.setAdapter(spinnerChoices);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // nearby requests
                    // same for riders & drivers
                    // TODO: feed in actual curr location
                    requests = RequestController.getNearbyRequests(new LatLng(53.5, -113.50));
                    adapter.notifyDataSetChanged();
                }
                else if (position == 1) {
                    // Accepted by Me (for drivers: by ME, for riders: by at least 1 driver
                    if (UserController.checkMode().equals(Mode.DRIVER)) {
                        requests = RequestController.getAcceptedByUser(UserController.getUser());
                    }
                    else {
                        requests = RequestController.getAcceptedByDrivers(UserController.getUser());
                    }
                    adapter.notifyDataSetChanged();
                }
                else if (position == 2) {
                    // confirmed requests
                    // if rider this will be requests the USER has confirmed after a driver has
                    // accepted them
                    // if driver, this will be requests ANOTHER USER has confirmed as a rider
                    // after accepted by the curr user
                    requests = RequestController.getConfirmedByRiders(UserController.getUser(),
                                UserController.checkMode());
                    adapter.notifyDataSetChanged();
                }
                else if (position == 3) {
                    // completed requests
                    // if driver, display completed as driver
                    // if rider, display completed as rider
                    requests = RequestController.getCompletedRequests(UserController.getUser(),
                                UserController.checkMode());
                    adapter.notifyDataSetChanged();
                }
                else if (position == 4) {
                    // only rider mode can get to this point.
                    requests = RequestController.getOwnRequests(UserController.getUser());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg) {
                // do nothing
            }
        });
        this.populateRequestList();
    }

    private void populateRequestList() {
        listView = (ListView) findViewById(R.id.requestList);
        // make it click!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                //TODO: some code here that responds to clicking on request
                // perhaps a dialog?
            }
        });
        adapter = new ArrayAdapter<UserRequest>(this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, this.requests);
        listView.setAdapter(adapter);

    }
}
