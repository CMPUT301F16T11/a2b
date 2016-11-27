package com.cmput301f16t11.a2b;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This activity is used to change the User's profile fields (email, phone no.)
 *
 */
public class EditProfileActivity extends AppCompatActivity {

    private User user;
    AlertDialog dialog;

    EditText locale;
    EditText phoneNum;
    EditText email;
    EditText make;
    EditText year;
    EditText color;
    EditText model;
    //TODO : WE can consider having profile picture implementation later for part 6

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

        TextView username = (TextView) findViewById(R.id.profile_name);

        locale = (EditText) findViewById(R.id.locale);
        locale.setKeyListener(null);
        phoneNum = (EditText) findViewById(R.id.phone_num);
        phoneNum.requestFocus();
        email = (EditText) findViewById(R.id.email);
        make = (EditText) findViewById(R.id.edit_make_field);
        year = (EditText) findViewById(R.id.edit_year_field);
        color = (EditText) findViewById(R.id.edit_color_field);
        model = (EditText) findViewById(R.id.edit_model_field);

        phoneNum.setText(user.getPhoneNumber(), TextView.BufferType.EDITABLE);
        email.setText(user.getEmail(), TextView.BufferType.EDITABLE);
        make.setText(user.getCar().getMake(), TextView.BufferType.EDITABLE);
        year.setText(Integer.toString(user.getCar().getYear()), TextView.BufferType.EDITABLE);
        color.setText(user.getCar().getColor(), TextView.BufferType.EDITABLE);
        model.setText(user.getCar().getModel(), TextView.BufferType.EDITABLE);

        username.setText(user.getName());
        locale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setMessage("Edit locale coming soon!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditProfileActivity.this.dialog.dismiss();
                        }
                    });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * Method which allows the user to edit their profile
     *
     * @param v the current view
     */
    public void editProfile(View v) {
        EditText userPhoneNumText = (EditText) findViewById(R.id.phone_num);
        EditText userEmailText = (EditText) findViewById(R.id.email);
        make = (EditText) findViewById(R.id.edit_make_field);
        year = (EditText) findViewById(R.id.edit_year_field);
        color = (EditText) findViewById(R.id.edit_color_field);
        model = (EditText) findViewById(R.id.edit_model_field);

        String phoneNumber = userPhoneNumText.getText().toString();
        String email = userEmailText.getText().toString();

        // car inputs
        String newMake = make.getText().toString();
        String newYear = year.getText().toString();
        String newColor = color.getText().toString();
        String newModel = model.getText().toString();

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

        if (!isValidYear(newYear)) {
            //Warn the user that you cannot use invalid info
            dlg.setMessage("That doesn't look like a valid year for car details.");
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
        Boolean changedMake = newMake.equals(user.getCar().getMake());
        Boolean changedYear = newYear.equals(user.getCar().getYear());
        Boolean changedColor = newColor.equals(user.getCar().getColor());
        Boolean changedModel = newModel.equals(user.getCar().getModel());

        //check if any updates have actually been made
        if(!changedEmail || !changedPhoneNumber || !changedMake || !changedYear || !changedColor ||
                !changedModel) {

            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);
            user.setCar(new Vehicle(newMake, newModel, newColor, Integer.parseInt(newYear)));
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
    private Boolean isValidYear(String number){
        Pattern format1 = Pattern.compile("[0-9]{4}"); // 7801234567
        if (format1.matcher(number).matches()) {
            return true;
        }
        return false;
    }

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
