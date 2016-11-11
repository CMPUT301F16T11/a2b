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


        // adding edit button functionality
        Button edit = (Button) findViewById(R.id.editProfile);
        edit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                launchProfileEditor();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        try {
            Intent intent = getIntent();
            String username = intent.getStringExtra("username");
            this.user = UserController.getUserFromName(username);
        } catch(Exception e) {
            this.user = UserController.getUser();
        }
        if (this.user == null) {
            this.user = UserController.getUser();
        }
        String usgh = this.user.getName();
        if (this.user.getName() == null) {
           this.user = UserController.getUser();
        }
        // set buttons
        Button editButton = (Button) findViewById(R.id.editProfile);
        if (user.equals(UserController.getUser())) {
            editButton.setEnabled(true);
        }
        else {
            editButton.setEnabled(false);
        }
        setTextViews();
    }

    private void launchProfileEditor() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    private void setTextViews() {

        //Populating text attributes
        TextView userNameTV = (TextView) findViewById(R.id.userName);
        TextView emailTV = (TextView) findViewById(R.id.emailText);
        TextView phoneNumberTV = (TextView) findViewById(R.id.phoneNumberTextView);

        userNameTV.setText(this.user.getName());
        emailTV.setText(this.user.getEmail());
        phoneNumberTV.setText(this.user.getPhoneNumber());
    }

}
