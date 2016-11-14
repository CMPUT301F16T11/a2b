package com.cmput301f16t11.a2b;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * This activity is used to change the User's profile fields (email, phone no.)
 *
 */
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

    /**
     * Sets the textview objects to display the current data.
     * Also sets these textviews to be editable.
     */
    public void setTexts() {
        // Edit Texts should be set to what the current profile has
        user = UserController.getUser();

        // populate with existing info
        EditText phoneNum = (EditText) findViewById(R.id.phone_num);
        EditText email = (EditText) findViewById(R.id.email);
        TextView username = (TextView) findViewById(R.id.profile_name);

        phoneNum.setText(user.getPhoneNumber(), TextView.BufferType.EDITABLE);
        email.setText(user.getEmail(), TextView.BufferType.EDITABLE);
        username.setText(user.getName());
    }

    /**
     * Method which allows the user to edit their profile
     *
     * @param v the current view
     */
    public void editProfile(View v) {
        EditText userPhoneNumText = (EditText) findViewById(R.id.phone_num);
        EditText userEmailText = (EditText) findViewById(R.id.email);

        String phoneNumber = userPhoneNumText.getText().toString();
        String email = userEmailText.getText().toString();

        AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.setTitle("Cannot Update Profile");


        if(!isValidEmail(email)){
            //Warn the user that you cannot update it with invalid info
            dlg.setMessage("The email is not valid please input a valid email.");
            dlg.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            dlg.show();
            return;
        }

        else
        if(!isValidPhoneNumber(phoneNumber)){
            //Warn the user that you cannot use invalid info
            dlg.setMessage("The phone number is not valid please input a valid phone number.");
            dlg.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            dlg.show();
            return;
        }

        Boolean changedPhoneNumber = phoneNumber.equals(user.getPhoneNumber());
        Boolean changedEmail = email.equals(user.getEmail());

        //check if any updates have actually been made
        if(!changedEmail || !changedPhoneNumber){

            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);
            UserController.setUser(user);
            UserController.updateUserInDb();
            UserController.saveInFile(this);
        }
        finish();
    }

    /**
     * Exits the EditProfile activity. Returns to ProfileActivity.
     *
     * @see ProfileActivity
     */
    public void cancelEdit(View v) {
        finish();
    }

    /**
     * Checks if a phone number is valid.
     *
     * @param number the number to check for validity
     * @return true if valid, false otherwise
     */
    private Boolean isValidPhoneNumber(String number){
        Pattern format1 = Pattern.compile("[0-9]{10}"); // 7801234567
        Pattern format2 = Pattern.compile("[0-9]{3}\\-[0-9]{3}\\-[0-9]{4}");

        if (format1.matcher(number).matches() || format2.matcher(number).matches()) {
            return true;
        }
        return false;
    }

    private Boolean isValidEmail(String email){
        Pattern format = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if(format.matcher(email).matches()){
            return true;
        }

        return false;
    }

    /**
     * Clear a textview (editable)
     *
     * @param v the textview in question
     */
    public void clearEditText(View v){
        EditText text = (EditText) v;
        text.setText("");
    }

}
