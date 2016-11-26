package com.cmput301f16t11.a2b;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created by jm on 2016-11-26.
 */

public class FileManager {

    // http://stackoverflow.com/questions/10402690/android-how-do-i-create-file-object-from-asset-file
    // nov 26
    public static void writeBytesToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while((nbread=is.read(data))>-1){
                fos.write(data,0,nbread);
            }
        }
        catch (Exception ex) {
            Log.e("Exception",ex.toString());
        }
        finally{
            if (fos!=null){
                fos.close();
            }
        }
    }

    public static void writeMapFile(Context context) {
        // taken from
        // http://stackoverflow.com/questions/23225431/how-to-return-file-object-from-assets-in-android
        // nov 26
        AssetManager assetManager = context.getAssets();

        // taken from kelvin's offline stuff
        try {
            FileOutputStream fos = context.openFileOutput("map.mbtiles", 0);
            //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("FileManager", "no map file found");
        } catch (IOException e) {
            Log.e("FileManager", e.toString());
        }
    }
}
