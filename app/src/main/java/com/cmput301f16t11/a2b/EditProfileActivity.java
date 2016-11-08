package com.cmput301f16t11.a2b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {


    User user;
    //TODO : WE can consider having profile picture implementation later for part 5

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // sets the edit text views of the profile view
        setTexts();


    }

    public void setTexts() {
        // Edit Texts should be set to what the current profile has

        user = UserController.getUser();

        // populate with existing info
        EditText editText = (EditText) findViewById(R.id.phone_num);
        editText.setText(user.getPhoneNumber(), TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.email);
        editText.setText(user.getEmail(), TextView.BufferType.EDITABLE);
        TextView text = (TextView) findViewById(R.id.profile_name);
        text.setText(user.getName());

    }

    public void editProfile(View v) {
        EditText userPhoneNumText = (EditText) findViewById(R.id.phone_num);
        EditText userEmailText = (EditText) findViewById(R.id.email);

        //check if any updates have actually been made
        if(userPhoneNumText.getText().toString().equals(user.getName()) == false  || userEmailText.getText().toString() != user.getPhoneNumber()){
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
