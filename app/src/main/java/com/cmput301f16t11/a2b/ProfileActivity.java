package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * ProfileActivity allows a user to view their own profile as well as though of other users
 */
public class ProfileActivity extends AppCompatActivity {
    /**
     * The following work, ProfileActivity, contains a derivative of an answer to
     * "How to make a phone call programatically?" by "Lior," edited by "The Alpha,"
     * users on stack overflow. It is used under CC-BY-SA by CMPUT301F16T11.
     *
     * Available here: http://stackoverflow.com/questions/4816683/how-to-make-a-phone-call-programatically
     * Date accessed: Nov. 11, 2016
     *
     * The following work, ProfileActivity, contains a derivative of android developer tutorial
     * "Requesting Permissions at Run Time." Used under Apache 2.0.
     *
     * Available here:
     * https://developer.android.com/training/permissions/requesting.html
     * Date accessed: Nov. 11, 2016
     *
     */
    private User user;
    private Context context;

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
        super.onResume();  // Always call the superclass method first;
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        if(username == null){
            user = UserController.getUser();
        }
        else{
            user = UserController.getUserFromName(username);
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

    /**
     * Launches the EditProfile activity if the user is on their own profile page
     *
     * @see EditProfileActivity
     */
    private void launchProfileEditor() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * Sets the textviews to display email, username and phone number
     */
    private void setTextViews() {

        //Populating text attributes
        TextView userNameTV = (TextView) findViewById(R.id.userName);
        TextView emailTV = (TextView) findViewById(R.id.emailText);
        TextView phoneNumberTV = (TextView) findViewById(R.id.phoneNumberTextView);

        userNameTV.setText(this.user.getName());
        emailTV.setText(this.user.getEmail());
        phoneNumberTV.setText(this.user.getFormattedPhoneNumber());
    }
}

