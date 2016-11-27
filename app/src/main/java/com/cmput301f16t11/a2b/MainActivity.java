package com.cmput301f16t11.a2b;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;

/**
 * Main activity to load activities for testing purposes.
 *
 * Will be depreciated in final product
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Testing Login Activity atm
        Intent intent = new Intent(this, LoginActivity.class);
        if(new File(CommandStack.ACCEPTFILE).exists()){
            CommandStack.setAcceptedCommands(FileController.loadFromFile(CommandStack.ACCEPTFILE, this));
        }
        if(new File(CommandStack.ADDFILE).exists()){
            CommandStack.setAcceptedCommands(FileController.loadFromFile(CommandStack.ADDFILE, this));
        }


        startActivity(intent);
        finish();
    }

}
