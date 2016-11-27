package com.cmput301f16t11.a2b;

/**
 * Created by john on 26/11/16.
 */

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

/**
 * Async task to queries the google maps server and get the distance from two lat lng points (road distance)
 */
public class DistanceCalculator extends AsyncTask<LatLng, Void, String> {
    @Override
    protected String doInBackground(LatLng ... location ){
        LatLng start = location[0];
        LatLng end = location[1];
        String url = JSONMapsHelper.createURl(start, end);

        String Json = "";
        try{
            Json = JSONMapsHelper.getJsonFromUrlRequest(url);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String distance = JSONMapsHelper.getDistance(Json);
        return distance;
    }
}