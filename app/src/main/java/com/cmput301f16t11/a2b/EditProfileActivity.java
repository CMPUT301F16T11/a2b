package com.cmput301f16t11.a2b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {


    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // sets the edit text views of the profile view
        //setTexts();
        user = UserController.getUser();

        // populate with existing info

        // Not sure if name should be editable
        EditText editText = (EditText) findViewById(R.id.profile_name);
        editText.setText(user.getName(), TextView.BufferType.EDITABLE);
        // Not sure if we need address here
        editText = (EditText) findViewById(R.id.address);
        editText.setText("insert address of user here", TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.phone_num);
        editText.setText(user.getPhoneNumber(), TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.email);
        editText.setText(user.getEmail(), TextView.BufferType.EDITABLE);

    }

    public void setTexts() {
        // Edit Texts should be set to what the current profile has

    }



    public void editProfile(View v) {
        EditText userPhoneNumText = (EditText) findViewById(R.id.phone_num);
        EditText userEmailText = (EditText) findViewById(R.id.email);

        //check if any updates have actually been made
        if(userPhoneNumText.getText().toString() != user.getName() || userEmailText.getText().toString() != user.getPhoneNumber()){
            user.setPhoneNumber(userPhoneNumText.getText().toString());
            user.setEmail(userEmailText.getText().toString());
            UserController.updateUserInDb();
        }

        this.finish();
    }

    public void cancelEdit(View view) {
        finish();
    }
}
