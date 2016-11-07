package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // adding edit button functionality
        Button edit = (Button) findViewById(R.id.editProfile);
        edit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                launchProfileEditor();
            }
        });

    }

    public void upDateActivity(){

    }

    private void logout(){

    }
    private void launchProfileEditor() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        // TODO user info must be put into the intent so that editprofile knows whats being edited
        //intent.putExtra("user", user);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

}
