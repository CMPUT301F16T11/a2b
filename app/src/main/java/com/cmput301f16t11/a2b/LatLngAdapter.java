package com.cmput301f16t11.a2b;



/**
 * Created by jm on 2016-11-26.
 */

public class LatLngAdapter {

    public static com.mapbox.mapboxsdk.geometry.LatLng getMapBoxLatLng(
            com.google.android.gms.maps.model.LatLng latLng) {
        return new com.mapbox.mapboxsdk.geometry.LatLng(latLng.latitude, latLng.longitude);
    }

    public static com.google.android.gms.maps.model.LatLng getGoogleLatLng(
            com.mapbox.mapboxsdk.geometry.LatLng latLng) {
        return new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(),
                latLng.getLongitude());
    }

}
