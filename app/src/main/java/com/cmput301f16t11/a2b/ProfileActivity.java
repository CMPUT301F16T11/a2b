package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

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
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first;
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        if (username == null) {
            user = UserController.getUserFromId(UserController.getUser().getId());
        } else {
            user = UserController.getUserFromName(username);
        }

        if (user.getId().equals(UserController.getUser().getId())) {
            UserController.setUser(user);
            UserController.saveInFile(this);
        }
        setTextViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (UserController.getUser().equals(user) && FileController.isNetworkAvailable(this)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.profile_menu, menu);

            // Taken from stackoverflow.com/questions/31953503/how-to-set-icon-color-of-menuitem on Nov 26 2016
            Drawable drawable = menu.getItem(0).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Only option available is profile editor
        if (item.getItemId()==R.id.edit_profile) {
            launchProfileEditor();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        TextView carYear = (TextView) findViewById(R.id.carYear);
        TextView carMake = (TextView) findViewById(R.id.carMake);
        TextView carModel = (TextView) findViewById(R.id.carModel);
        TextView carColor = (TextView) findViewById(R.id.carColor);
        TextView rating = (TextView) findViewById(R.id.ratingText);

        Vehicle vehicle = user.getCar();
        try {
            carYear.setText(String.valueOf(vehicle.getYear()));
            carMake.setText(vehicle.getMake());
            carModel.setText(vehicle.getModel());
            carColor.setText(vehicle.getColor());
        } catch (NullPointerException e) {
            carYear.setVisibility(View.GONE);
            carMake.setVisibility(View.GONE);
            carModel.setVisibility(View.GONE);
            carColor.setVisibility(View.GONE);
        }

        userNameTV.setText(this.user.getName());
        emailTV.setText(this.user.getEmail());
        phoneNumberTV.setText(this.user.getFormattedPhoneNumber());
        if(this.user.getRating() != -1) {
            rating.setText(new DecimalFormat("##.##").format(this.user.getRating()) + "/5");
        }
        else{
            rating.setText("Unrated");
        }
    }
}

