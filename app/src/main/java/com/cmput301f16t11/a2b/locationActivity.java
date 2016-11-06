package com.cmput301f16t11.a2b;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Main activity for riders to select their pickup and drop off locations
 */
public class locationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker tripStartMarker;
    private Marker tripEndMarker;
    private Marker currentMarker;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;
    }

    /**
     * Set up all the on click listeners for this activity
     */
    private void setButtonListeners(){
        final Button editProfileButton = (Button)findViewById(R.id.editProfile);
        final Button setLocation = (Button)findViewById(R.id.setLocationButton);
        final Button cancelTrip = (Button)findViewById(R.id.cancelTrip);
        final ToggleButton driverModeToggle = (ToggleButton) findViewById(R.id.driverModeToggle);
        cancelTrip.setEnabled(false);

        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripStartMarker != null){
                    tripStartMarker.remove();
                    tripStartMarker = null;
                }
                if(tripEndMarker != null){
                    tripEndMarker.remove();
                    tripEndMarker = null;
                }
                if(currentMarker != null){
                    currentMarker.remove();
                    currentMarker = null;
                }

                //Reset button state when they cancel
                setLocation.setText(R.string.set_start);

                //Once they cancel it disable this button until they place another pin
                cancelTrip.setEnabled(false);

                mMap.clear();
            }
        });

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do not want to spawn a warning if the end marker is not null
                if(currentMarker == null && tripEndMarker == null){
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
                else if(tripStartMarker == null){
                    currentMarker.remove();
                    tripStartMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentMarker.getPosition())
                            .title(getString(R.string.start_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    tripStartMarker.showInfoWindow();
                    currentMarker = null;
                    setLocation.setText(R.string.set_end);
                    cancelTrip.setEnabled(true);
                }

                else if(tripEndMarker == null){
                   currentMarker.remove();
                    tripEndMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentMarker.getPosition())
                            .title(getString(R.string.end_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    tripEndMarker.showInfoWindow();
                    currentMarker = null;
                    setLocation.setText(R.string.confirm_trip);

                }

                else{

                    JSONMapsHelper helper = new JSONMapsHelper(mMap);
                    helper.drawPathCoordinates(tripStartMarker, tripEndMarker);
                    //TODO: confirmation of trip do something with the lat and long
                }

            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: edit profile activity goes here
            }
        });

//        driverModeToggle.setOnClickListener(

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
                .zoom(11)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        setButtonListeners();
    }
}

/**
 * This class is used in create the JSON request from a list of lats and long and then parsing through
 * the results returned from hte google maps. Please not most of this code is taken from:
 * http://stackoverflow.com/questions/14702621/answer-draw-path-between-two-points-using-google-maps-android-api-v2
 */
 class JSONMapsHelper{
    private GoogleMap map;

    public JSONMapsHelper(GoogleMap map){
        this.map = map;
    }

    public void drawPathCoordinates(Marker startLocation, Marker endLocation){

        String url = createURl(startLocation, endLocation);
        urlParser impl  = new urlParser();
        impl.execute(url);
    }
    /**
     * This functions creates a URL JSON request from two google map markers
     * @param startLocation
     * @param endLocation
     * @return A valid String url request for calculated route
     */
     private String createURl(Marker startLocation, Marker endLocation){
        double latStart = startLocation.getPosition().latitude;
        double lonStart = startLocation.getPosition().longitude;
        double latEnd   = endLocation.getPosition().latitude;
        double lonEnd   = endLocation.getPosition().longitude;

        StringBuilder url = new StringBuilder();
        url.append("https://maps.googleapis.com/maps/api/directions/json");
        url.append("?origin=");// from
        url.append(Double.toString(latStart));
        url.append(",");
        url.append(Double.toString(lonStart));
        url.append("&destination=");// to
        url.append(Double.toString(latEnd));
        url.append(",");
        url.append(Double.toString(lonEnd));
        url.append("&sensor=false&mode=driving&alternatives=true");
        url.append("&key=AIzaSyCcJvOdnYtQ9ES2-SQRYlHoHgGnc46Pfco");

        return url.toString();
    }

    /**
     * This function connects with google server and get a JSON obkect able to draw the route
     * from a proper URL request.
     * @param url
     * @return JSON string object
     */
    public String getJsonFromUrlRequest(String url){

        //Make the url request alot of things can go wrong here
        HttpURLConnection urlConnection = null;
        String results = "";
        try{
            URL validUrl = new URL(url);
            urlConnection = (HttpURLConnection) validUrl.openConnection();
            urlConnection.connect();

            BufferedReader is = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            results = readInStream(is);
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return results;
    }

    /**
     * Read in the http request from the website
     * @param is Buffered Reader of input stream
     * @return return the results from the reader
     */
    private String readInStream(BufferedReader is){

        try {
            StringBuilder builder = new StringBuilder();

            //Loop through the reader and get all the lines
            String currentLine;
            while ((currentLine = is.readLine()) != null) {
                builder.append(currentLine + "\n");
            }

            is.close();
            return builder.toString();
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Get all the draw results from JSON string
     * @param result result from google server request
     * @return latlng list of all te points to draw lines to
     */
    public List<LatLng>  getDrawPath(String result) {
        List<LatLng> list;
        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            list = decodePoly(encodedString);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * draw all the lines on a map
     * @param result all the points to draw lines to and from
     */
    private void drawLinesOnMap(List<LatLng> result){
        map.addPolyline( new PolylineOptions()
                        .addAll(result)
                        .width(12)
                        .color(Color.parseColor("#05b1fb"))//Google maps blue color
                        .geodesic(true)
                    );
    }

    /**
     * This is just copied and pasted from :http://stackoverflow.com/questions/14702621/answer-draw-path-between-two-points-using-google-maps-android-api-v2
     * Decoded poly line from jason object and turns them into a more undertsandable latLng list
     * @param encoded
     * @return
     */
    private List<LatLng> decodePoly(String encoded){
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    /**
     * private async task so the html requests are run in the background and not on ui thread
     */
    private class urlParser extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String ... url){
            String Json = "";
            try{
                Json = getJsonFromUrlRequest(url[0]);
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return Json;
        }

        @Override
        protected void onPostExecute(String Result) {
            super.onPostExecute(Result);
            drawLinesOnMap(getDrawPath(Result));
        }


    }
}
