package com.cmput301f16t11.a2b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // sets the edit text views of the profile view
        setTexts();

    }

    public void setTexts() {
        // Edit Texts should be set to what the current profile has
        EditText editText = (EditText) findViewById(R.id.profile_name);
        editText.setText("insert profile name here from file", TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.address);
        editText.setText("insert address of user here", TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.phone_num);
        editText.setText("insert phone_num of user here", TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.email);
        editText.setText("insert email of user here", TextView.BufferType.EDITABLE);
    }

    public void editProfile() {

    }

    public void cancelEdit(View view) {
        finish();
    }
}
