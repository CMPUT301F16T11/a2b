package com.cmput301f16t11.a2b;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main map activity for riders to select their pickup and drop off locations.
 */
public class RiderLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
            DrawingLocationActivity {

    private GoogleMap mMap;
    private Marker tripStartMarker;
    private Marker tripEndMarker;
    private Marker currentMarker;
    private Context context;
    private String tripDistance = "? km";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewProfile:
                Intent profileIntent = new Intent(RiderLocationActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;

            case R.id.changeRole:

                Intent driverIntent = new Intent(RiderLocationActivity.this, DriverLocationActivity.class);
                UserController.setMode(Mode.DRIVER);
                startActivity(driverIntent);
                finish();
                return true;

            case R.id.viewRequests:
                Intent requestIntent = new Intent(RiderLocationActivity.this, RequestListActivity.class);
                startActivity(requestIntent);
                return true;

            case R.id.signOut:
                UserController.logOut(this);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_rider_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startUpNotificationService();

    }

    public void startUpNotificationService(){
        ArrayList<UserRequest>  currentOpenRequests = RequestController.getOwnActiveRequests(UserController.getUser());

        //Start the rider service if it is not already started start it up and add all own active requests
        if(!RiderNotificationService.isRecieveServiceStarted()) {
            Intent intent = RiderNotificationService.createIntentStartNotificationService(context);
            startService(intent);

            for(UserRequest request : currentOpenRequests){
                RiderNotificationService.addRequestToBeNotified(request);
            }
        }
    }

    /**
     * Set up all the on click listeners for this activity
     */
    private void setButtonListeners() {
        final Button setLocation = (Button) findViewById(R.id.setLocationButton);
        final Button cancelTrip = (Button) findViewById(R.id.cancelTrip);
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        cancelTrip.setEnabled(false);

        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetMap();
            }
        });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                LatLng location = place.getLatLng();
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                //Move the camera to where they searched
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place.getLatLng())      // Sets the center of the map to location user
                        .zoom(11)
                        .bearing(0)
                        .tilt(0)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                //Put a marker where they searched
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(place.getAddress().toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                currentMarker.showInfoWindow();
            }

            @Override
            public void onError(Status status) {
                AlertDialog markerWarning = new AlertDialog.Builder(context).create();
                markerWarning.setMessage("Something went wrong in trying to search address.");
                markerWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                markerWarning.show();
            }
        });

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do not want to spawn a warning if the end marker is not null
                if (currentMarker == null && tripEndMarker == null) {
                    AlertDialog markerWarning = new AlertDialog.Builder(context).create();
                    markerWarning.setMessage(getString(R.string.warning_message));
                    markerWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    markerWarning.show();
                }

                //We know we are setting our start
                else if (tripStartMarker == null) {
                    currentMarker.remove();
                    String address = getString(R.string.start_location) + ": " + currentMarker.getTitle();
                    tripStartMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentMarker.getPosition())
                            .title(address)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    tripStartMarker.showInfoWindow();
                    currentMarker = null;
                    setLocation.setText(R.string.set_end);
                    cancelTrip.setEnabled(true);
                } else if (tripEndMarker == null) {
                    currentMarker.remove();
                    String address = getString(R.string.end_location) + ": " + currentMarker.getTitle();
                    tripEndMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentMarker.getPosition())
                            .title(address)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    tripEndMarker.showInfoWindow();
                    currentMarker = null;
                    setLocation.setText(R.string.confirm_trip);

                    JSONMapsHelper helper = new JSONMapsHelper((RiderLocationActivity) context);
                    helper.drawPathCoordinates(tripStartMarker, tripEndMarker);

                    setLocation.setEnabled(false);

                } else {

                    displayRideConfirmationDlg(tripDistance);
                }

            }
        });

        //Listener for the map so we know when the user clicks
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                if (currentMarker != null) {
                    currentMarker.remove();
                }
                if (tripEndMarker == null) {
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                    try {
                        Geocoder geoCoder = new Geocoder(context);
                        List<Address> matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        String address = "";
                        if (!matches.isEmpty()) {
                            address = matches.get(0).getAddressLine(0) + ' ' + matches.get(0).getLocality();
                        }

                        currentMarker.setTitle(address);
                        currentMarker.showInfoWindow();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    /**
     * When the map is initialized this is called by default
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Set the initial spot to edmonton for now
        LatLng edmonton = new LatLng(53.5444, -113.4909);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(edmonton)      // Sets the center of the map to location user
                .zoom(11)
                .bearing(0)
                .tilt(0)
                .build();

        ensureLocationPermissions();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        setButtonListeners();
    }

    /**
     * A quick permission check to ensure that we location services enabled
     */
    private void ensureLocationPermissions() {
        //Check if we have the right permissions to use location
        Boolean i = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (i) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * This method is called after the route has been retrieved from the google servers. It creates and shows
     * the confirmation_trip dialog which prompts the user to enter a fare amount. It also draws polyline points
     * on the map of the route they will be taking.
     *
     * @param drawPoints list of LatLng objs to draw points of
     * @param distance distance between
     */
    public void drawRouteOnMap(List<LatLng> drawPoints, String distance) {
        tripDistance = distance;

        //We want them to be unable to push the set location button until distance has been properly calcuated
        Button setLocationBut = (Button)findViewById(R.id.setLocationButton);
        setLocationBut.setEnabled(true);

        //Draw the lines on the map
        mMap.addPolyline(new PolylineOptions()
                .addAll(drawPoints)
                .width(12)
                .color(Color.parseColor("#05b1fb"))//Google maps blue color
                .geodesic(true)
        );

    }

    public void displayRideConfirmationDlg(final String distance){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.rider_confirmation_dialog);

        final TextView from = (TextView)dialog.findViewById(R.id.rideConf_startText);
        final TextView to = (TextView)dialog.findViewById(R.id.rideConf_endText);
        final TextView distanceDlg = (TextView)dialog.findViewById(R.id.rideConf_distanceText);
        //final EditText description = (EditText)dialog.findViewById(R.id.rideConf_descripText);
        final Button cancel = (Button)dialog.findViewById(R.id.cancelRequest);
        final Button confirm = (Button)dialog.findViewById(R.id.confirmRequest);
        final EditText amount= (EditText)dialog.findViewById(R.id.rideConf_fareText);

        //Set all the right values in the dialog
        final double fairAmount = FairEstimation.estimateFair(distance);
        String fromText = "";
        String endText = "";
        try {
            Geocoder geoCoder = new Geocoder(context);
            List<Address> start = geoCoder.getFromLocation(tripStartMarker.getPosition().latitude, tripStartMarker.getPosition().longitude, 1);
            List<Address> end = geoCoder.getFromLocation(tripEndMarker.getPosition().latitude, tripEndMarker.getPosition().longitude, 1);
            if(!start.isEmpty()){
                fromText = start.get(0).getAddressLine(0);
            }
            if(!end.isEmpty()) {
                endText = end.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            Log.i("Error", "Unable to decode address");
            e.printStackTrace();
        }
        from.setText(fromText);
        to.setText(endText);
        distanceDlg.setText(distance);
        amount.setText(Double.toString(fairAmount));
        amount.setHint(Double.toString(fairAmount));

        //set up the on click listeners for dialog


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("");
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double userFare = 0.00;
                try {
                    userFare = Double.parseDouble(amount.getText().toString());

                } catch (NumberFormatException e) {
                    //Tell them they have not entered a double
                    e.printStackTrace();
                }
                if (userFare == 0.00) {
                    AlertDialog doubleWarning = new AlertDialog.Builder(context).create();
                    doubleWarning.setMessage(getString(R.string.double_warning_message));
                    doubleWarning.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    doubleWarning.show();
                    return;
                }

                //This code grabs the double from a string "12.7 km"
                Scanner sc = new Scanner(distance);
                double doubleDistance = sc.nextDouble();

                UserRequest request = new UserRequest(tripStartMarker.getPosition(),
                        tripEndMarker.getPosition(),
                        userFare,
                        UserController.getUser().getId(),
                        doubleDistance);
                RequestController.addOpenRequest(request);

                //Add this request to be monitored
                RiderNotificationService.addRequestToBeNotified(request);

                //Clear the map
                resetMap();

                //
                dialog.dismiss();
            }
        });

        //Show the dialog
        dialog.show();
    }

    /**
     * Resets the map to default opener
     */
    public void resetMap() {
        final Button setLocation = (Button) findViewById(R.id.setLocationButton);
        final Button cancelTrip = (Button) findViewById(R.id.cancelTrip);
        if (tripStartMarker != null) {
            tripStartMarker.remove();
            tripStartMarker = null;
        }
        if (tripEndMarker != null) {
            tripEndMarker.remove();
            tripEndMarker = null;
        }
        if (currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
        }

        //Reset button state when they cancel
        setLocation.setText(R.string.set_start);

        //Once they cancel it disable this button until they place another pin
        cancelTrip.setEnabled(false);

        mMap.clear();
    }

    /**
     * A class to estimate a recommended fare between two points
     */
    static class FairEstimation {

        /**
         * Estimates fair fare between two points
         *
         * @param strDistance distance between the points
         * @return double of fair fare
         */
        static public double estimateFair(String strDistance) {
            Scanner sc = new Scanner(strDistance);
            double distanceKm = sc.nextDouble();

            //Rate starts at 2.50
            double rate = 2.50;

            //add 1.25 for each km
            rate += distanceKm * 1.25;

            DecimalFormat df = new DecimalFormat("#.##");
            String dx = df.format(rate);
            rate = Double.valueOf(dx);

            return rate;
        }
    }
}





