package com.cmput301f16t11.a2b;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A view for a map activity to show locations when clicked. Used to show locations within the
 * app instead of created a map intent using the phones default map app
 */
public class EmptyMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location locationToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationToShow = (Location) getIntent().getSerializableExtra("Location");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Marker temp = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(locationToShow.getLat(), locationToShow.getLon()))
                .title(locationToShow.getAddress())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(locationToShow.getLat(), locationToShow.getLon()))      // Sets the center of the map to location user
                .zoom(11)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        temp.showInfoWindow();
    }
}
