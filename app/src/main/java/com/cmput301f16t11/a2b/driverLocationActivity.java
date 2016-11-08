package com.cmput301f16t11.a2b;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class driverLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private HashMap<Marker, UserRequest> requestMap = new HashMap<Marker, UserRequest>();

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try{
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch(SecurityException e){
            e.printStackTrace();
        }
        if (mLastLocation != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(11)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Load requests within a 9 square km area
            // 110.574 = km per latitude degree
            // 111.320cos(longitude degrees) = km per longitude degree
            double lowerLat = mLastLocation.getLatitude() - (3/110.574);
            double higherLat = mLastLocation.getLatitude() + (3/110.574);
            double lowerLon = mLastLocation.getLongitude() - (3/111.320*Math.cos(mLastLocation.getLongitude()));
            double higherLon = mLastLocation.getLongitude() + (3/111.320*Math.cos(mLastLocation.getLongitude()));

            ArrayList<UserRequest> nearbyRequests = new ArrayList<UserRequest>();
            ElasticsearchRequestController.GetNearbyRequests getNearbyRequests = new ElasticsearchRequestController.GetNearbyRequests();
            getNearbyRequests.execute(lowerLat, higherLat, lowerLon, higherLon);
            try {
                nearbyRequests = getNearbyRequests.get();
                handleRequests(nearbyRequests);
            } catch (Exception e) {
                Log.i("Error", "AsyncTask failed to execute");
            }
        }
    }

    /**
     * Adds a marker for every request on the list
     * Adds an entry to the hashmap for every (marker, request) pair
     *
     * @param list : ArrayList<UserRequest>
     */
    public void handleRequests(ArrayList<UserRequest> list) {
        // Clear mapping of old requests
        requestMap.clear();
        mMap.clear();

        // Add marker, clickListener, and hashmap entry for each request
        for (UserRequest req : list) {
            Marker tmp = mMap.addMarker(new MarkerOptions()
            .position(req.getStartLocation())
            .title(req.getFare().toString()));

            // Display marker info dialog onClick
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    MarkerInfoDialog dialog = MarkerInfoDialog.newInstance(requestMap.get(marker));
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

        }
    }

