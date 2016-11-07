package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the user info from User controller
        user = UserController.getUser();

        //Populating text attrubutes
        TextView userNameTV = (TextView) findViewById(R.id.userName);
        TextView emailTV = (TextView) findViewById(R.id.emailText);
        TextView phoneNumberTV = (TextView) findViewById(R.id.phoneNumberTextView);
        userNameTV.setText(user.getName());
        emailTV.setText(user.getEmail());
        phoneNumberTV.setText(user.getPhoneNumber());

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
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

}
