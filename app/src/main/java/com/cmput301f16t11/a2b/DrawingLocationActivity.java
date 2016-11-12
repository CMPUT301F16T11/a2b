package com.cmput301f16t11.a2b;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Interface implemented for drawing routes on maps.
 * <p>
 * @see DriverLocationActivity
 * @see RiderLocationActivity
 */

public interface DrawingLocationActivity {

    /**
     * drawRouteOnMap method declaration. The only method which must be implemented when
     * implementing this interface.
     * <p>
     * @param drawPoints a list of the points to be drawn on the map
     * @param distance the distance between points
     */
    public void drawRouteOnMap(List<LatLng> drawPoints, String distance);

}
