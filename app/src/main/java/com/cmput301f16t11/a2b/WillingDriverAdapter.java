package com.cmput301f16t11.a2b;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Extends ArrayAdapter and modifies view so every even numbered entry is lightly shaded in
 * This work, "ShadedListAdapter," contains a derivative of an answer to
 * "Android Alternate row Colors in ListView" by "Suraj Bajaj," a user on Stack Overflow.
 * It is used under CC-BY-SA by CMPUT301F16T11.
 * It is available here:
 * http://stackoverflow.com/questions/13109840/android-alternate-row-colors-in-listview
 */

public class WillingDriverAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    public WillingDriverAdapter(Context cntxt, ArrayList<User> objs) {
        super(cntxt, 0, objs);
        users = objs;
        context = cntxt;
    }

    @Override
    public View getView(int pos, View v, ViewGroup vGroup) {
        // Get the data item for this position
        User user = users.get(pos);

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.willing_driver_item, vGroup, false);
        }

        if (pos % 2 == 0) {
            // if even, shade the background
            v.setBackgroundColor(Color.rgb(224, 224, 224));
        }
        else{
            v.setBackgroundColor(Color.WHITE);
        }

        return setViews(v, user);
    }

    private View setViews(View view, User user){

        final TextView driverEntry = (TextView) view.findViewById(R.id.driver_entry);
        final TextView ratingEntry  = (TextView) view.findViewById(R.id.rating_entry);
        String driverRatingString = "";
        Double driverRating = 0.0;

        driverRating = user.getRating();
        if(driverRating.equals(new Double(-1))){
            driverRatingString = "No rating available";
        }else {
            driverRatingString = driverRating.toString();
        }



        try {
            driverEntry.setText(user.getName());
            ratingEntry.setText(driverRatingString);

        } catch (NullPointerException e) {
            Log.e("Shaded ERROR:", "No user found in request");
        }

        return view;
    }

}
