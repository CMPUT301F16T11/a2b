package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * This activity displays a list of requests which corresponds to the current user mode as well
 * as the selected requests to view in the spinner (also in this activity)
 *
 * This work, "RequestListActivity," contains a derivative
 * "Android - How to create clickable listview?" by "Delpes," a stackoverflow user,
 * used under CC-BY-SA by CMPUT301F16T11.
 * (Available here:
 * http://stackoverflow.com/questions/13281197/android-how-to-create-clickable-listview)
 * Accessed Nov. 11, 2016
 *
 * This work, "RequestListActivity," contains a derivative of example code from "Radio Buttons,"
 * from Android Developer Tutorials. It is used under Apache 2.0 by CMPUT301F16T11.
 * Available here:
 * https://developer.android.com/guide/topics/ui/controls/radiobutton.html
 * Accessed Nov. 18, 2016
 *
 * This work, "RequestListActivity," contains a derivative of example code from "Show Keyboard
 * Automatically," answer by "Feonix," a stackoverflow user. Used under CC-BY-SA by
 * CMPUT301F16T11.
 * Available here:
 * http://stackoverflow.com/questions/14759253/show-keyboard-automatically
 * Accessed Nov. 18, 2016
 *
 * STATE DESIGN PATTERN
 */
public class RequestListActivity extends AppCompatActivity {

    private ArrayList<UserRequest> requests;
    private ShadedListAdapter adapter;
    private ListView listView;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerChoices;
    private EditText maxPricePerKM;
    private EditText maxPrice;
    private Boolean filterMaxPrice;
    private Boolean filterMaxPricePerKM;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       if(!FileController.isNetworkAvailable(this) && UserController.checkMode() == Mode.DRIVER){
           MenuInflater inflater = getMenuInflater();
           inflater.inflate(R.menu.offline_request_detail, menu);
       }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.changeRole:
                Intent driverIntent = new Intent(this, RiderLocationActivity.class);
                UserController.setMode(Mode.RIDER);
                startActivity(driverIntent);
                finish();
                break;
            case R.id.refresh:
                if (FileController.isNetworkAvailable(this)) {
                    if (FileController.isNetworkAvailable(this)) {
                        if (CommandStack.workRequired()) {
                            RunCommandStackProgressDialog();
                        }
                    }
                    Intent intent = new Intent(this, DriverLocationActivity.class);
                    startActivity(intent);
                    //We want to finish if we are going into online mode
                    finish();
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(this).create();
                    dialog.setTitle(getString(R.string.offline_mode));
                    dialog.setMessage(getString(R.string.offline_message));
                    dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                }
                break;
            case R.id.signOut:
                UserController.logOut(this);
                finish();
                }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        requests = new ArrayList<>();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {
        //TODO: Refactor (extraction)
        super.onResume();
        hideKeyboard();
        this.filterMaxPrice = Boolean.FALSE;
        this.filterMaxPricePerKM = Boolean.FALSE;
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

        if (UserController.checkMode() == Mode.DRIVER) {
            // EditText stuff
            maxPrice = (EditText) findViewById(R.id.insert_max_price);
            maxPricePerKM = (EditText) findViewById(R.id.insert_max_price_per_km);
            maxPrice.setEnabled(false);
            maxPricePerKM.setEnabled(false);
            maxPricePerKM.setText(R.string.empty_wallet);
            maxPrice.setText(R.string.empty_wallet);
            maxPricePerKM.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        filterMaxPricePerKM = true;
                        filterMaxPrice =false;
                        updateData();
                        hideKeyboard();
                        return true;
                    }
                    return false;
                }
            });
            maxPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        filterMaxPrice = true;
                        filterMaxPricePerKM = false;
                        spinner.setSelection(spinner.getSelectedItemPosition(), false);
                        updateData();
                        hideKeyboard();
                        return true;
                    }
                    return false;
                }
            });
            hideKeyboard();
        }

        // spinner stuff
        if (UserController.checkMode() == Mode.DRIVER && !FileController.isNetworkAvailable(this)) {
            // offline mode, accepted by me only
            String [] choices;
            choices = getResources().getStringArray(R.array.requestTypesOfflineDriver);
            spinnerChoices = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, choices);
        }
        else if (UserController.checkMode() == Mode.RIDER && !FileController.isNetworkAvailable(this)) {
            String [] choices;
            choices = getResources().getStringArray(R.array.requestTypesOfflineRider);
            spinnerChoices = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                    choices);
        }
        else if (UserController.checkMode() == Mode.DRIVER) {
            String [] choices = getResources().getStringArray(R.array.requestTypesDriver);
            spinnerChoices = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, choices);
        } else {
            // rider
            String[] choices = getResources().getStringArray(R.array.requestTypesRiderArray);
            spinnerChoices = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, choices);
        }

        adapter = new ShadedListAdapter(this, this.requests);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spinner = (Spinner) findViewById(R.id.requestSpinner);
        spinner.setFocusable(true);
        spinner.requestFocus();
        spinner.setAdapter(spinnerChoices);
        final Context context = this;
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                    // Driver Mode
                    if (UserController.checkMode() == Mode.DRIVER) {
                        if (FileController.isNetworkAvailable(context)) {
                            try {
                                requests.clear();
                                requests.addAll(
                                        getFilteredRequests(RequestController.getNearbyRequests()));
                                try {
                                    requests.remove(RequestController.getDeletedRequest());
                                } catch (Exception e) {
                                    Log.i("RequestList", "No deleted data was being shown. Carry on");
                                }
                            } catch (NullPointerException e) {
                                Log.i("No requests found", "driver mode");
                            }
                        }
                        else {
                            // already accepted requests
                            requests.clear();
                            requests.addAll(getFilteredRequests(RequestController.getNearbyRequests()));
                            try {
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        }

                    }
                    else {
                        // rider mode!
                        // my requests
                        if (FileController.isNetworkAvailable(context)) {
                            requests.clear();
                            requests.addAll(
                                    RequestController.getOwnActiveRequests(UserController.getUser(), RequestListActivity.this));
                            try {
                                UserRequest request = RequestController.getDeletedRequest();
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        }
                        else {
                            // Pending requests
                            requests.clear();
                            requests.addAll(
                                RequestController.getOfflineRequests());
                            try {
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        }

                    }
                    adapter.notifyDataSetChanged();
                } else if (position == 1) {
                    // Accepted by Me (for drivers: by ME, for riders: by at least 1 driver
                    if (UserController.checkMode() == Mode.DRIVER) {
                        if (FileController.isNetworkAvailable(context)) {
                            requests.clear();
                            requests.addAll(
                                getFilteredRequests(RequestController.getAcceptedByUser(
                                        UserController.getUser(), RequestListActivity.this)));
                            try {
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        }
                        else {
                            // offline mode
                            // Accepted by me
                            requests.clear();
                            requests.addAll(getFilteredRequests(RequestController.getAcceptedByUser(UserController.getUser(),
                                    context)));
                            try {
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        }
                    } else {
                        // users
                        requests.clear();
                        if(FileController.isNetworkAvailable(RequestListActivity.this)) {
                            requests.addAll(RequestController.getAcceptedByDrivers(UserController.getUser(), RequestListActivity.this));
                            try {
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        } else {
                            requests.addAll(RequestController.getOwnActiveRequests(UserController.getUser(), RequestListActivity.this));
                            try {
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
//                    populateRequestList();
                } else if (position == 2) {
                    // confirmed requests
                    // if rider this will be requests the USER has confirmed after a driver has
                    // accepted them
                    // if driver, this will be requests ANOTHER USER has confirmed as a rider
                    // after accepted by the curr user
                    if (UserController.checkMode() == Mode.RIDER ||
                            FileController.isNetworkAvailable(context)) {
                        requests.clear();
                        requests.addAll(
                                getFilteredRequests(RequestController.getConfirmedByRiders(
                                        UserController.getUser(), UserController.checkMode())));
                        try {
                            requests.remove(RequestController.getDeletedRequest());
                        } catch (Exception e) {
                            Log.i("RequestList", "No deleted data was being shown. Carry on");
                        }
                    }
                    else if(UserController.checkMode() == Mode.RIDER ||
                            !FileController.isNetworkAvailable(context)) {
                        requests.clear();
                        requests.addAll(getFilteredRequests(
                                RequestController.getCompletedRequests(UserController.getUser(),
                                        UserController.checkMode(), RequestListActivity.this)));
                        try {
                            requests.remove(RequestController.getDeletedRequest());
                        } catch (Exception e) {
                            Log.i("RequestList", "No deleted data was being shown. Carry on");
                        }
                    }
                    else {
                        if(FileController.isNetworkAvailable(context)) {
                            requests.clear();
                            requests.addAll(RequestController.getCompletedRequests(
                                    UserController.getUser(), UserController.checkMode(), context));
                            try {
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        }
                         //Offline acceptances driver
                        else {
                            requests.clear();
                            requests.addAll(RequestController.getOfflineAcceptances());
                            try {
                                requests.remove(RequestController.getDeletedRequest());
                            } catch (Exception e) {
                                Log.i("RequestList", "No deleted data was being shown. Carry on");
                            }
                        }

                    }
                    adapter.notifyDataSetChanged();
//                    populateRequestList();
                } else if (position == 3) {
                    if(UserController.checkMode() == Mode.DRIVER) {
                        requests.clear();
                        requests.addAll(getFilteredRequests(
                                RequestController.getAwaitingPaymentRequests(UserController.getUser(),
                                        UserController.checkMode())));
                        try {
                            requests.remove(RequestController.getDeletedRequest());
                        } catch (Exception e) {
                            Log.i("RequestList", "No deleted data was being shown. Carry on");
                        }
                    }
                    //Completed Requests
                    else if (!FileController.isNetworkAvailable(context)){
                        requests.clear();
                        requests.addAll(getFilteredRequests(RequestController.getCompletedRequests(UserController.getUser(),
                                        UserController.checkMode(), RequestListActivity.this)));
                        try {
                            requests.remove(RequestController.getDeletedRequest());
                        } catch (Exception e) {
                            Log.i("RequestList", "No deleted data was being shown. Carry on");
                        }
                    }
                    else {
                        requests.clear();
                        requests.addAll(getFilteredRequests(
                                RequestController.getAwaitingPaymentRequests(UserController.getUser(),
                                        UserController.checkMode())));
                        try {
                            requests.remove(RequestController.getDeletedRequest());
                        } catch (Exception e) {
                            Log.i("RequestList", "No deleted data was being shown. Carry on");
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                else if (position == 4) {
                    // completed requests
                    // if driver, display completed as driver
                    // if rider, display completed as rider
                    requests.clear();
                    requests.addAll(getFilteredRequests(
                            RequestController.getCompletedRequests(UserController.getUser(),
                            UserController.checkMode(), RequestListActivity.this)));
                    adapter.notifyDataSetChanged();
                    try {
                        requests.remove(RequestController.getDeletedRequest());
                    } catch (Exception e) {
                        Log.i("RequestList", "No deleted data was being shown. Carry on");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg) {
                // do nothing
            }
        });
    }

    public void radioButtonOnClickListener(View v) {
        // here is where the above cited android developers tutorial code is used
        // check if button is checked
        boolean checked = ((RadioButton) v).isChecked();
        v.clearFocus();

        // which button?
        switch(v.getId()) {
            case R.id.request_list_no_filter:
                if (checked) {
                    hideKeyboard();
                    maxPrice.setEnabled(false);
                    maxPrice.setText(R.string.empty_wallet);
                    maxPricePerKM.setEnabled(false);
                    maxPricePerKM.setText(R.string.empty_wallet);
                    this.filterMaxPricePerKM = false;
                    this.filterMaxPrice = false;
                    v.clearFocus();
                    updateData();
                    break;
                }
            case R.id.request_list_max_price:
                if (checked) {
                    maxPrice.setEnabled(true);
                    maxPricePerKM.setText(R.string.empty_wallet);
                    maxPrice.requestFocus();
                    maxPrice.setText("");
                    showKeyboard(maxPrice);
                    maxPricePerKM.setEnabled(false);
                    this.filterMaxPricePerKM = false;
                    this.filterMaxPrice = true;
                    break;
                }
            case R.id.request_list_max_price_per_km:
                if (checked) {
                    maxPricePerKM.setEnabled(true);
                    maxPricePerKM.setText(R.string.empty_wallet);
                    maxPricePerKM.requestFocus();
                    maxPricePerKM.setText("");
                    showKeyboard(maxPricePerKM);
                    maxPrice.setEnabled(false);
                    this.filterMaxPrice = false;
                    this.filterMaxPricePerKM = true;
                    break;
                }
        }
    }

    private void showKeyboard(EditText editText) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {// Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private ArrayList<UserRequest> getFilteredRequests(ArrayList<UserRequest> listOfRequests) {
        ArrayList<UserRequest> tempList = listOfRequests;
        
        if (this.filterMaxPricePerKM) {
            tempList.clear();
            for (UserRequest request: listOfRequests) {
                if (RequestController.getPricePerKM(request) <= this.getCurrentPricePerKM()) {
                    tempList.add(request);
                }
            }
        }
        else if (this.filterMaxPrice) {
            tempList.clear();
            for (UserRequest request: listOfRequests) {
                if (request.getFare().doubleValue() <= this.getCurrentPrice()) {
                    tempList.add(request);
                }
            }
        }
        return tempList;
    }

    private double getCurrentPricePerKM() {
        try {
            return Double.parseDouble(this.maxPricePerKM.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double getCurrentPrice() {
        try {
            return Double.parseDouble(this.maxPrice.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateData() {
        // click another entry in the spinner and then the original one to reload data
        int pos = spinner.getSelectedItemPosition();
        if (pos == 3) {
            spinner.setSelection(pos - 1, false);
        }
        else {
            spinner.setSelection(pos + 1, false);
        }
        spinner.setSelection(pos, false);
        try {
            requests.remove(RequestController.getDeletedRequest());
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.i("RequestList", "no deleted requests in list");
        }
    }

    /**
     * Run the command stack stuff in the background with a progress dialog. This ensures the user does not
     * think the app crashed.
     */
    private void RunCommandStackProgressDialog(){
        final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.refreshing), getString(R.string.rider_refreshing_message), false);
        final Context context = this;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                CommandStack.handleStack(context);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }

}
