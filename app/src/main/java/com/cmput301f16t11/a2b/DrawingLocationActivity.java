package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by john on 10/11/16.
 */

public interface DrawingLocationActivity {

    public void drawRouteOnMap(List<LatLng> drawPoints, String distance);

}
