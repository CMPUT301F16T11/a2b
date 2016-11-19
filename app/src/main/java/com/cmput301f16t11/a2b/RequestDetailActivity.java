package com.cmput301f16t11.a2b;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
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

/**
 * This activity displays the details of a request and allows interaction with it:
 * accept a driver, delete a request, confirm a driver, enable payment, complete a request,
 * accept a request
 */
public class RequestDetailActivity extends AppCompatActivity {
    /**
     * This work, "RequestDetailActivity," contains a derivative
     * "Android - How to create clickable listview?" by "Delpes," a stackoverflow user,
     * used under CC-BY-SA by CMPUT301F16T11.
     * (Available here:
     * http://stackoverflow.com/questions/13281197/android-how-to-create-clickable-listview)
     *
     * The following dialog contained in "RequestDetailActivity" is a derivative of an answer to
     * "How to display a Yes/No dialog box on Android?" by "Steve Haley," a user on
     * stack overflow, used under CC-BY-SA by CMPUT301F16T11.
     * Available here:
     * http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
     */
    // potentially a dialog with a complete button is applicable
    // if not a view user button
    private UserRequest request;
    private ListView driverList; // TODO: populate this list
    private ArrayList<User> acceptedDrivers;
    private int currPosition;

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

    /**
     * populates list of drivers who have accepted this request <p>
     * Allows the click of a driver to create dialog to allow user to confirm drier acceptance
     */
    public void populateAcceptedDriversList() {
        final Context context = this;
        acceptedDrivers = RequestController.getAcceptedDrivers(request);
        driverList = (ListView) findViewById(R.id.accepted_list);
        driverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                currPosition = position;
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                RequestController.setRequestConfirmedDriver(request,
                                        acceptedDrivers.get(currPosition),
                                        RequestDetailActivity.this);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                // do nada
                                break;
                        }
                    }
                };
                String messageString = "Accept " + acceptedDrivers.get(currPosition).getName() +
                                        " as your driver?";
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(messageString)
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                // the below dialog wasn't working. I don't know how it was set up to work so
                // i created the above dialog in the meanwhile for actual functionality.
                // At whatever point someone can re-enable the below dialog as long as they get it
                // working. (since visually its more consistent with the app)
                /*
                final User driver = acceptedDrivers.get(position);
                final Dialog dlg = new Dialog(context);
                dlg.setContentView(R.layout.request_details_confirm_driver_dlg);

                final TextView username = (TextView)dlg.findViewById(R.id.driverUsername);
                final Button cancelButton = (Button)dlg.findViewById(R.id.cancelDriver);
                final Button confirmButton = (Button)dlg.findViewById(R.id.acceptDriver);

                username.setText(driver.getName());

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestController.setRequestConfirmedDriver(request, driver,
                                RequestDetailActivity.this);
                    }
                });
                */

            }
        });
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, acceptedDrivers);

        driverList.setAdapter(adapter);
    }

    /**
     * populates the textviews with (sometimes clickable) request information
     */
    public void populateFields() {
        TextView driverName = (TextView) findViewById(R.id.request_detail_driver);
        if (request.getConfirmedDriver() != null) {
            driverName.setText(
                    UserController.getUserFromId(request.getConfirmedDriver()).getName());
            driverName.setTextColor(Color.rgb(6, 69, 173));
            driverName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RequestDetailActivity.this, ProfileActivity.class);
                    intent.putExtra("username",
                            UserController.getUserFromId(request.getConfirmedDriver()).getName());
                    startActivity(intent);
                }
            });
        } else {
            driverName.setText("No confirmed driver :(");
        }

        TextView riderName = (TextView) findViewById(R.id.request_detail_rider);
        riderName.setText(UserController.getUserFromId(request.getRider()).getName());
        riderName.setTextColor(Color.rgb(6, 69, 173));
        riderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDetailActivity.this, ProfileActivity.class);
                intent.putExtra("username",
                        UserController.getUserFromId(request.getRider()).getName());
                startActivity(intent);
            }
        });

        TextView startLocation = (TextView) findViewById(R.id.request_detail_pickup);
        final String location_string_start = getLocationString(request.getStartLocation());
        startLocation.setText(location_string_start);
        startLocation.setTextColor(Color.rgb(6, 69, 173));
        startLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDetailActivity.this, EmptyMapActivity.class);
                Location location = new Location(request.getStartLocation().latitude, request.getStartLocation().longitude, location_string_start);
                intent.putExtra("Location", location);
                startActivity(intent);
            }
        });

        TextView endLocation = (TextView) findViewById(R.id.request_detail_dropoff);
        final String location_string_end = getLocationString(request.getEndLocation());
        endLocation.setText(location_string_end);
        endLocation.setTextColor(Color.rgb(6, 69, 173));
        endLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDetailActivity.this, EmptyMapActivity.class);
                Location location = new Location(request.getEndLocation().latitude, request.getEndLocation().longitude, location_string_end);
                intent.putExtra("Location", location);
                startActivity(intent);
            }
        });

        TextView fare = (TextView) findViewById(R.id.request_detail_fare);
        fare.setText("$" + request.getFare().toString());

    }

    /**
     * Gets an address from a LatLng obj using geocoder
     *
     * @param location the LatLng of the location to get an address for
     * @return String of the address
     */
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

    /**
     * Sets up buttons on detail page and has logic to check if they should be clickable or not
     */
    public void setButtons() {
        Button deleteButton = (Button) findViewById(R.id.request_detail_delete);
        Button acceptButton = (Button) findViewById(R.id.request_detail_accept);
        Button completeButton = (Button) findViewById(R.id.request_detail_complete);
        Button payButton = (Button) findViewById(R.id.request_detail_pay);
        if (UserController.checkMode() ==
                Mode.RIDER && UserController.getUser().equals(request.getRider())) {
            deleteButton.setEnabled(true);
            completeButton.setEnabled(false);
            if (request.hasConfirmedRider()) {
                payButton.setEnabled(true);
            }
            else {
                payButton.setEnabled(false);
            }
        }
        else if (UserController.checkMode() == Mode.DRIVER &&
                request.getConfirmedDriver() != null &&
                    request.getConfirmedDriver().equals(UserController.getUser())) {
            completeButton.setEnabled(true);
            payButton.setEnabled(false);
        }
        else {
                payButton.setEnabled(false);
                deleteButton.setEnabled(false);
                completeButton.setEnabled(false);
        }
        // accept
        if (UserController.checkMode() == Mode.DRIVER &&
                !request.getAcceptedDrivers().contains(UserController.getUser())) {
            acceptButton.setEnabled(true);
        } else {
            acceptButton.setEnabled(false);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

            // onClick Listeners
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestController.addAcceptance(request, RequestDetailActivity.this);
                    finish();
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete();
                }
            });
            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestController.completeRequest(request);
                }
            });
    }

    /**
     * Called when delete button is pressed. Creates dialog confirming deletion of request.
     * Then deletes request
     */
    private void delete () {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete this ride request?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRequest();
                    }
                })
                .create();
        dialog.show();
    }
    private void deleteRequest(){
        RequestController.deleteRequest(request.getId());
        finish();
        }
}
