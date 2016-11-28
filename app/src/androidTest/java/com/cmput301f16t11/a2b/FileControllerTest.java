package com.cmput301f16t11.a2b;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

/**
 * Created by kelvinliang on 2016-11-27.
 */

@RunWith(AndroidJUnit4.class)
public class FileControllerTest  {
    Context context = null;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getContext();
    }

    @Test
    public void SaveRequestsTest() {
        ArrayList<UserRequest> requests = new ArrayList<UserRequest>();
        UserRequest request1 = new UserRequest(new LatLng(123, 123), new LatLng(34, 21), 10, "kf3123");
        UserRequest request2 = new UserRequest(new LatLng(123, 321), new LatLng(12, 21), 10, "kf1432");
        requests.add(request1);
        requests.add(request2);



        File file = new File(Environment.getExternalStorageDirectory(),  "test.sav");

        try {
            file.createNewFile();
        } catch (Exception e) {

        }
        Long length1 = file.length();
        FileController.saveInFile(requests, file.getAbsolutePath(), context);
        Long length2 = file.length();
        assertTrue(length2 > length1);
        //context.deleteFile("test.sav");
    }
//
//    @Test
//    public void LoadRequestsTest() {
//        ArrayList<UserRequest> requests = new ArrayList<UserRequest>();
//        UserRequest request1 = new UserRequest(new LatLng(123, 123), new LatLng(34, 21), 10, "kf3123");
//        UserRequest request2 = new UserRequest(new LatLng(123, 321), new LatLng(12, 21), 10, "kf1432");
//        requests.add(request1);
//        requests.add(request2);
//
//        FileController.saveInFile(requests, "test.sav", context);
//        ArrayList<UserRequest> requests2 = FileController.loadFromFile("test.sav", context);
//        assertTrue(requests2.get(0).getRiderID().equals(requests2.get(0).getRiderID()));
//        assertTrue(requests2.get(1).getRiderID().equals(requests2.get(1).getRiderID()));
//    }
}
