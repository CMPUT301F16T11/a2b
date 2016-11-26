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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main map activity for riders to select their pickup and drop off locations.
 */
public class OfflineRiderLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private OfflineManager offlineManager;
    private Marker tripStartMarker;
    private Marker tripEndMarker;
    private Marker currentMarker;
    private Context context;
    private String tripDistance = "? km";

    private int LOCATION_PERMISSIONS = -1;

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
                Intent profileIntent = new Intent(OfflineRiderLocationActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;

            case R.id.changeRole:
                if (UserController.canDrive()) {
                    Intent driverIntent = new Intent(OfflineRiderLocationActivity.this, DriverLocationActivity.class);
                    UserController.setMode(Mode.DRIVER);
                    startActivity(driverIntent);
                    finish();
                }
                else {
                    showNotADriverDialog();
                }
                return true;

            case R.id.viewRequests:
                Intent requestIntent = new Intent(OfflineRiderLocationActivity.this, RequestListActivity.class);
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
        /*
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission==PackageManager.PERMISSION_DENIED) {
            Boolean waiting = true;

            while (waiting) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS);
            }
        }*/

        context = this;

        setContentView(R.layout.activity_rider_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // set up the offline manager
                offlineManager = OfflineManager.getInstance(OfflineRiderLocationActivity.this);

                // only save edmonton data for now...
                // based on edmonton corporate boundaries
                // https://data.edmonton.ca/Administrative/City-of-Edmonton-Corporate-Boundary/m45c-6may/data
                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                        .include(new LatLng(53.5170575986615, -113.375141910159)) // NE
                        .include(new LatLng(53.5170575986615, -113.375141910159))
                        .build();

                OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                        "mapbox://styles/cmput301f16t11/civyw083g004i2kpcqsnr6hux",
                        latLngBounds,
                        10,
                        20,
                        OfflineRiderLocationActivity.this.getResources().getDisplayMetrics().density);

                byte[] metadata;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("FIELD_REGION_NAME", "Edmonton");
                    String json = jsonObject.toString();
                    metadata = json.getBytes("UTF-8");
                } catch (Exception exception) {
                    Log.e("OfflineLocation", "Failed to encode metadata: " + exception.getMessage());
                    metadata = null;
                }

                offlineManager.createOfflineRegion(
                        definition,
                        metadata,
                        new OfflineManager.CreateOfflineRegionCallback() {
                            @Override
                            public void onCreate(OfflineRegion offlineRegion) {
                                offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);

//                                // Display the download progress bar
//                                progressBar = (ProgressBar) findViewById(R.id.progress_bar);
//                                startProgress();

                                // Monitor the download progress using setObserver
                                offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                                    @Override
                                    public void onStatusChanged(OfflineRegionStatus status) {

                                        // Calculate the download percentage and update the progress bar
                                        double percentage = status.getRequiredResourceCount() >= 0
                                                ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                                0.0;

                                        if (status.isComplete()) {
                                            // Download complete
//                                            endProgress("Region downloaded successfully.");
                                            Log.i("isComplete", "Download complete");
                                        } else if (status.isRequiredResourceCountPrecise()) {
                                            // Switch to determinate state
                                            Log.i("percentage", String.valueOf(percentage));
//                                            setPercentage((int) Math.round(percentage));
                                        }
                                    }

                                    @Override
                                    public void onError(OfflineRegionError error) {
                                        // If an error occurs, print to logcat
                                        Log.e("OfflineLocation", "onError reason: " + error.getReason());
                                        Log.e("OfflineLocation", "onError message: " + error.getMessage());
                                    }

                                    @Override
                                    public void mapboxTileCountLimitExceeded(long limit) {
                                        // Notify if offline region exceeds maximum tile count
                                        Log.e("OfflineLocation", "Mapbox tile count limit exceeded: " + limit);
                                    }
                                });
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("OfflineLocation", "Error: " + error);
                            }
                        });
            }
        });
    }

    @Override
    public void onResume() {

    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
                LatLng location = LatLngAdapter.getMapBoxLatLng(place.getLatLng());
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
                        .position(LatLngAdapter.getGoogleLatLng(location))
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

//        //Listener for the map so we know when the user clicks
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                if (currentMarker != null) {
//                    currentMarker.remove();
//                }
//                if (tripEndMarker == null) {
//                    currentMarker = mMap.addMarker(new MarkerOptions()
//                            .position(LatLngAdapter.getGoogleLatLng(latLng))
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
//
//                    try {
//                        Geocoder geoCoder = new Geocoder(context);
//                        List<Address> matches = geoCoder.getFromLocation(latLng.getLatitude(),
//                                latLng.getLongitude(), 1);
//                        String address = "";
//                        if (!matches.isEmpty()) {
//                            address = matches.get(0).getAddressLine(0) + ' ' + matches.get(0).getLocality();
//                        }
//
//                        currentMarker.setTitle(address);
//                        currentMarker.showInfoWindow();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//        });
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
                .target(LatLngAdapter.getGoogleLatLng(edmonton))      // Sets the center of the map to location user
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

//    /**
//     * This method is called after the route has been retrieved from the google servers. It creates and shows
//     * the confirmation_trip dialog which prompts the user to enter a fare amount. It also draws polyline points
//     * on the map of the route they will be taking.
//     *
//     * @param drawPoints list of LatLng objs to draw points of
//     * @param distance distance between
//     */
//    public void drawRouteOnMap(List<LatLng> drawPoints, String distance) {
//        tripDistance = distance;
//
//        //We want them to be unable to push the set location button until distance has been properly calcuated
//        Button setLocationBut = (Button)findViewById(R.id.setLocationButton);
//        setLocationBut.setEnabled(true);
//
//        //Draw the lines on the map
//        mMap.addPolyline(new PolylineOptions()
//                .addAll(drawPoints)
//                .width(12)
//                .color(Color.parseColor("#05b1fb"))//Google maps blue color
//                .geodesic(true)
//        );
//
//    }

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
        final EditText description = (EditText)dialog.findViewById(R.id.rideConf_descripText);

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
                ArrayList<String> locationList = RequestController.searchLocationName(
                        tripStartMarker.getPosition(),tripEndMarker.getPosition(),context);

                UserRequest request = new UserRequest(tripStartMarker.getPosition(),
                        tripEndMarker.getPosition(),
                        userFare,
                        UserController.getUser().getId(),
                        doubleDistance,
                        description.getText().toString(),
                        locationList.get(0),locationList.get(1));

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

    private void showNotADriverDialog() {
        final Context context = this;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.not_a_driver).setPositiveButton("OK", dialogClickListener).show();

    }
}





