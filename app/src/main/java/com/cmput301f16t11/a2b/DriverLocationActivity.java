package com.cmput301f16t11.a2b;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This activity allows the driver to view a map, place pins, and search around
 * those pins a specific radius to see any requests in that specified area.
 * It also has currentSearchCriteriaa settings bar that allows the user to view profile see a more detailed
 * list of requests or log out.
 *
 */
public class DriverLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DrawingLocationActivity{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker currentMarker;
    private Circle currentCircle;
    private int currentSearchRadius = 3000; // defaults to 3000m
    private Context context;
    private SearchCriteria currentSearchCriteria;
    private HashMap<Marker, UserRequest> requestMap = new HashMap<Marker, UserRequest>();


    //This is used within search by keyword dialog
    public enum SearchCriteria{
        START,
        END,
        DESCRIPTION,
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

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
                Intent profileIntent = new Intent(DriverLocationActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;

            case R.id.changeRole:
                Intent driverIntent = new Intent(DriverLocationActivity.this, RiderLocationActivity.class);
                UserController.setMode(Mode.RIDER);

                startActivity(driverIntent);
                finish();
                return true;

            case R.id.viewRequests:
                Intent requestIntent = new Intent(DriverLocationActivity.this, RequestListActivity.class);
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
    public void onResume() {
        super.onResume();
        if (!FileController.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, RequestListActivity.class);
            setResult(Activity.RESULT_OK, intent);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * This method sets up the places auto complete bar implemented by google places
     * api.
     */
    private void setUpAutoCompleteBar(){
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Move the camera to where they searched
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place.getLatLng())      // Sets the center of the map to location user
                        .zoom(11)
                        .bearing(0)
                        .tilt(0)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                PlaceMarker(place.getLatLng());
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
    }

    /**
     * This method sets up the listeners to the map item. Specifically the location click
     * button.
     */
    public void setUpMapClicking(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                PlaceMarker(latLng);
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                try{
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if(mLastLocation != null) {
                        LatLng newLocation = (new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                        PlaceMarker(newLocation);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(newLocation)      // Sets the center of the map to location user
                                .zoom(11)                   // Sets the zoom
                                .bearing(0)                // Sets the orientation of the camera to east
                                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }catch(SecurityException e){
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try{
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch(SecurityException e){
            e.printStackTrace();
        }
        //We only want to load their current location if this is the first time
        if (mLastLocation != null && currentMarker == null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(11)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Load requests within a 9 square km area
            // 110.574 = km per latitude degree
            LatLng currentLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            ArrayList<UserRequest> requests = generateRequests(3000, currentLatLng);
            handleRequests(requests);

            //Place a marker at the start at it last known location
            PlaceMarker(currentLatLng);
        }
    }

    /**
     * This method generate all the open rider requests withing a specified area.
     *
     * @param radiusMeters (int) radius of the search
     * @param center (LatLng) center spot of the search
     * @return a collection of user request within that area on the map
     */
    public ArrayList<UserRequest> generateRequests(int radiusMeters, LatLng center){
        double distanceKm = radiusMeters/1000;
        ArrayList<UserRequest> nearbyRequests = new ArrayList<>();

        nearbyRequests = RequestController.getNearbyRequestsGeoFilter(distanceKm, center.latitude, center.longitude );
        RequestController.setDisplayedRequests(nearbyRequests, this);

        return nearbyRequests;
    }

    /**
     * Adds a marker for every request on the list
     * Adds an entry to the hashmap for every (marker, request) pair
     *
     * @param list : ArrayList<UserRequest>
     */
    public void handleRequests(ArrayList<UserRequest> list) {
        // Clear mapping of old requests
        for(Marker m: requestMap.keySet()){
            m.remove();
        }
        requestMap.clear();

        // Add marker, clickListener, and hashmap entry for each request
        for (UserRequest req : list) {
            Marker tmp = mMap.addMarker(new MarkerOptions()
                    .position(req.getStartLocation())
                    .title(req.getFare().toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            // Display marker info dialog onClick
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(marker.equals(currentMarker)){
                        return true;
                    }

                    MarkerInfoDialog dialog = MarkerInfoDialog.newInstance(requestMap.get(marker));
                    dialog.setCancelable(true);
                    dialog.show(getFragmentManager().beginTransaction(), "dialog");
                    return true;
                }
            });

            requestMap.put(tmp, req);
        }
    }

    @Override
    public void onConnectionSuspended(int i){

    }

    @Override
    public void onConnectionFailed(ConnectionResult result){

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Check if we have the right permissions to use location
        Boolean i = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (i) {
            mMap.setMyLocationEnabled(true);
        }

        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            mMap.setMyLocationEnabled(true);
        }

        setUpAutoCompleteBar();
        setUpMapClicking();
        setListeners();
    }

    /**
     * Set up all the listeners for the entire activity
     */
    private void setListeners(){
        final SeekBar radius = (SeekBar) findViewById(R.id.radiusSeekBar);
        final TextView radiusSet = (TextView) findViewById(R.id.radiusText);
        final Button  searchNearPin = (Button) findViewById(R.id.searchNearPin);
        final Button  searchByKeyword = (Button) findViewById(R.id.searchByKeyword);

        radius.setMax(10000);
        radius.setProgress(3000);
        radiusSet.setText("3000" + "meters");

        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBar.setProgress(progress);
                    currentSearchRadius = progress;
                    radiusSet.setText(String.valueOf(progress) + "meters");
                }

                if(currentCircle != null){
                    currentCircle.setRadius(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        searchNearPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( currentMarker == null){
                    //DO Nothing maybe display a dialog telling them to place a pin on map
                }
                else{
                    ArrayList<UserRequest> requests = generateRequests(currentSearchRadius, currentMarker.getPosition());
                    handleRequests(requests);
                }
            }
        });

        searchByKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.search_by_keyword);
                setSearchByKeyWordOnClickListeners(dialog);
                dialog.show();
            }
        });
    }


    /**
     * Set up the listeners for specific search by keyword dialog instance
     * @param dialog
     */
    private void setSearchByKeyWordOnClickListeners(final Dialog dialog) {
        final RadioGroup selectorGroup = (RadioGroup) dialog.findViewById(R.id.searchGroup);
        final Button okButton = (Button) dialog.findViewById(R.id.okKeyword);
        final Button cancelButton = (Button) dialog.findViewById(R.id.cancelKeyword);
        final EditText entry = (EditText) dialog.findViewById(R.id.textKeyword);

        //defaults to start location
        currentSearchCriteria = SearchCriteria.START;

        selectorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.descriptionSelect: {
                        currentSearchCriteria = SearchCriteria.DESCRIPTION;

                        return;
                    }
                    case R.id.startSelect: {
                        currentSearchCriteria = SearchCriteria.START;
                        return;
                    }
                    case R.id.endSelect: {
                        currentSearchCriteria = SearchCriteria.END;
                        return;
                    }
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchString = entry.getText().toString();
                //Make sure their search string is not empty
                if(searchString.isEmpty()){
                    AlertDialog dlg = new AlertDialog.Builder(context).create();
                    dlg.setMessage(getString(R.string.keyword_warning_message));
                    dlg.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    dlg.show();
                    return;
                }

                switch(currentSearchCriteria){
                    case DESCRIPTION: {
                        ArrayList<UserRequest> descriptionList = RequestController.queryByKeywordDescription(searchString);
                        //show the requests in this list
                        RequestController.setDisplayedRequests(descriptionList, DriverLocationActivity.this);
                        //update the map
                        handleRequests(descriptionList);
                        break;
                    }
                    case START: {
                        ArrayList<UserRequest> startKeywordList = RequestController.queryByKeywordStartLocation(searchString);
                        //show the requests in this list
                        RequestController.setDisplayedRequests(startKeywordList, DriverLocationActivity.this);
                        //update the map
                        handleRequests(startKeywordList);
                        break;
                    }
                    case END: {
                        ArrayList<UserRequest> endKeywordList = RequestController.queryByKeywordEndLocation(searchString);
                        //show the requests in this list
                        RequestController.setDisplayedRequests(endKeywordList, DriverLocationActivity.this);
                        //update the map
                        handleRequests(endKeywordList);
                        break;
                    }
                }

                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this is set the parent activity will know the user cancelled
                dialog.dismiss();
            }
        });

    }

    /**
     * Moves the current marker location to the latlng location. This will move the circle radius and also
     * remove the prior marker if there is one. Also uses a geocoder to determine the actual address.
     *
     * @param latlng marker location
     */
    public void PlaceMarker(LatLng latlng){
        if(currentMarker != null){
            currentMarker.remove();
        }

        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        //adjust the circle or set it
        if(currentCircle == null){
            CircleOptions circleOptions = new CircleOptions()
                    .center(currentMarker.getPosition())
                    .radius(currentSearchRadius);

            currentCircle = mMap.addCircle(circleOptions);
        }
        else
        {
            currentCircle.setCenter(currentMarker.getPosition());
            currentCircle.setRadius(currentSearchRadius);
        }

        try {
            Geocoder geoCoder = new Geocoder(context);
            List<Address> matches = geoCoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
            String address = "";
            if (!matches.isEmpty()) {
                address = matches.get(0).getAddressLine(0) + matches.get(0).getLocality();
            }

            currentMarker.setTitle(address);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * IMplements drawRouteOnMap
     * <p>
     * @see DrawingLocationActivity
     * @param drawPoints a list of the points to be drawn on the map
     * @param distance the distance between points
     */
    public void drawRouteOnMap(List<LatLng> drawPoints, String distance){

        //Draw the lines on the map
        mMap.addPolyline( new PolylineOptions()
                .addAll(drawPoints)
                .width(12)
                .color(Color.parseColor("#05b1fb"))//Google maps blue color
                .geodesic(true)
        );
    }
}

