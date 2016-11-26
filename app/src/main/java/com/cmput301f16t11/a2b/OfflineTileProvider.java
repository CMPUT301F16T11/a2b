package com.cmput301f16t11.a2b;

import android.util.Log;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jm on 2016-11-26.
 */

// based on http://stackoverflow.com/questions/13695393/open-street-maps-with-android-google-maps-api-v2
// nov. 26
// EVENTUALLY WILL HAVE SAVED TILES
public class OfflineTileProvider extends UrlTileProvider{

    private String baseUrl;

    public OfflineTileProvider(int width, int height, String url) {
        super(width, height);
        this.baseUrl = url;
    }

    @Override
    public URL getTileUrl(int x, int y, int zoom) {
        try {
            return new URL(baseUrl.replace("{z}", ""+zoom).replace("{x}",""+x).replace("{y}",""+y));
        } catch (MalformedURLException e) {
            Log.e("tileprovider", "bad url");
        } catch (Exception e) {
            Log.e("tileprovider", "other error");
        }
        return null;
    }
}
