package com.cmput301f16t11.a2b;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import com.google.gson.Gson;
import java.util.ArrayList;

/**
 * This activity displays a list of requests which corresponds to the current user mode as well
 * as the selected requests to view in the spinner (also in this activity)
 *
 * This work, "MainHabitActivity," contains a derivative
 * "Android - How to create clickable listview?" by "Delpes," a stackoverflow user,
 * used under CC-BY-SA by CMPUT301F16T11.
 * (Available here:
 * http://stackoverflow.com/questions/13281197/android-how-to-create-clickable-listview)
 * Accessed Nov. 11, 2016
 *
 * This work, "MainHabitActivity," contains a derivative of example code from "Radio Buttons,"
 * from Android Developer Tutorials. It is used under Apache 2.0 by CMPUT301F16T11.
 * Available here:
 * https://developer.android.com/guide/topics/ui/controls/radiobutton.html
 * Accessed Nov. 18, 2016
 *
 */
public class RequestListActivity extends AppCompatActivity {
    /**

     */
    private ArrayList<UserRequest> requests;
    private ShadedListAdapter adapter;
    private ListView listView;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerChoices;
    private EditText maxPricePerKM;
    private EditText maxPrice;
    private RadioButton noFilterButton;
    private RadioButton maxPriceFilterButton;
    private RadioButton maxPricePerKMFilterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        requests = new ArrayList<UserRequest>();
    }

    @Override
    public void onResume() {
        //TODO: Refactor (extraction)
        super.onResume();
        // listView Stuff
        // two content views (depending on driver vs rider)
        if (UserController.checkMode() == Mode.DRIVER) {
            setContentView(R.layout.activity_request_list_driver);
        }
        else {
            setContentView(R.layout.activity_request_list);
        }
        requests.clear();
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
        // Radio Button Stuff
        noFilterButton = (RadioButton) findViewById(R.id.request_list_no_filter);
        maxPriceFilterButton = (RadioButton) findViewById(R.id.request_list_max_price);
        maxPricePerKMFilterButton = (RadioButton) findViewById(R.id.request_list_max_price_per_km);

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

        adapter = new ShadedListAdapter(this, this.requests);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spinner = (Spinner) findViewById(R.id.requestSpinner);
        spinner.setAdapter(spinnerChoices);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                    // Driver Mode
                    if (UserController.checkMode() == Mode.DRIVER) {
                        try {
                            requests.clear();
                            requests.addAll(RequestController.getNearbyRequests());
                        } catch (NullPointerException e) {
                            Log.i("No requests found", "requestlistactivity");
                        }
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

    public void radioButtonOnClickListener(View v) {

    }
}
