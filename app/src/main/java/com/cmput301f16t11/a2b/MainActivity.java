package com.cmput301f16t11.a2b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TEMPORARY CODE - LAUNCHES LOGIN ACTIVITY FOR TESTING
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
