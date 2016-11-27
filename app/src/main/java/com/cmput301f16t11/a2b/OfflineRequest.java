package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by john on 26/11/16.
 */

public class OfflineRequest {
    private LatLng start;
    private LatLng end;
    private double fare;
    private String description;

    OfflineRequest(LatLng start, LatLng end, Double fare, String description){
        this.start = start;
        this.end = end;
        this.fare = fare;
        this.description = description;
    }

    public LatLng getStart(){
        return start;
    }

    public LatLng getEnd(){
        return end;
    }

    public Double getFare(){
        return fare;
    }

    public String getDescription(){
        return description;
    }
}
