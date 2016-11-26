package com.cmput301f16t11.a2b;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
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

    public static String writeMapFile(Context context) {
        // taken from
        // http://stackoverflow.com/questions/5943916/files-from-res-file-to-sdcard-on-android
        // nov 26
        InputStream ins = context.getResources().openRawResource (R.raw.map);
        String filename = "";
        try {
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();
            filename = Environment.getExternalStorageDirectory().toString() + File.separator + "map";
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            Log.e("FM", e.toString());
        }
        return filename;
    }
}
