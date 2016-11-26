package com.cmput301f16t11.a2b;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 * Viewed on November 7, 2016
 * https://developer.android.com/guide/topics/ui/dialogs.html
 *
 * Takes a UserRequest & is created by newInstance
 * Idea for ^ taken on November 7, 2016 from
 * http://stackoverflow.com/questions/15459209/passing-argument-to-dialogfragment
 *
 * To implement this dialog you must do the following in your code:
 * RideCompleteDialog newDialog = RideCompleteDialog.newInstance(UserRequest);
 * newDialog.show(getFragmentManager().beginTransaction(), "dialog");
 */

public class RideCompleteDialog extends DialogFragment {

    private View layout;
    private TextView driver;
    private TextView rider;
    private TextView pickup;
    private TextView dropoff;
    private TextView fare;
    private Button okButton;
    private ArrayList<ImageView> starList = new ArrayList<>();

    private UserRequest req;
    private int rating = 0;
    private String confirmedDriverName;
    private String riderName;

    /**
     * Static creator method that assigns UserRequest to dialog
     *
     * @param req : UserRequest
     * @return dialog : RideCompleteDialog
     */
    public static RideCompleteDialog newInstance(UserRequest req) {
        RideCompleteDialog dialog = new RideCompleteDialog();

        Bundle args = new Bundle();
        User confirmedDriver = UserController.getUserFromId(req.getConfirmedDriverID());
        User rider = UserController.getUserFromId(req.getRiderID());
        args.putString("driver", confirmedDriver.getName());
        args.putString("rider", rider.getName());
        args.putParcelable("req", req);
        dialog.setArguments(args);

        return dialog;
    }

    /**
     * Method to inflate the dialog layout
     *
     * @param savedInstanceState : Bundle
     * @return builder.create() : Dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the ride info
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Set the view
        layout = inflater.inflate(R.layout.dialog_ride_complete, null);
        builder.setView(layout);

        // Assign views, get request and set views
        assignViews();
        req = getArguments().getParcelable("req");
        confirmedDriverName = getArguments().getString("driver");
        riderName = getArguments().getString("rider");
        setViews();

        return builder.create();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return layout;
    }


    /**
     * Finds all views from the dialog
     */
    public void assignViews() {
        driver = (TextView) layout.findViewById(R.id.dialog_rideComplete_driverText);
        rider = (TextView) layout.findViewById(R.id.dialog_rideComplete_riderText);
        pickup = (TextView) layout.findViewById(R.id.dialog_rideComplete_startText);
        dropoff = (TextView) layout.findViewById(R.id.dialog_rideComplete_endText);
        fare = (TextView) layout.findViewById(R.id.dialog_rideComplete_fareText);
        okButton = (Button) layout.findViewById(R.id.dialog_rideComplete_button);
        starList.add((ImageView) layout.findViewById(R.id.dialog_rideComplete_1));
        starList.add((ImageView) layout.findViewById(R.id.dialog_rideComplete_2));
        starList.add((ImageView) layout.findViewById(R.id.dialog_rideComplete_3));
        starList.add((ImageView) layout.findViewById(R.id.dialog_rideComplete_4));
        starList.add((ImageView) layout.findViewById(R.id.dialog_rideComplete_5));
    }

    /**
     *  Assigns TextViews values based on req
     *
     *  Sets okButton click listener
     */
    public void setViews() {
        driver.setText(confirmedDriverName);
        rider.setText(riderName);
        pickup.setText(req.getStartLocationName());
        dropoff.setText(req.getEndLocationName());
        fare.setText(req.getFare().toString());

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (rating != 0) {
                    UserController.updateRating(rating, confirmedDriverName);
                }
                RideCompleteDialog.this.getDialog().dismiss();
                getActivity().finish();
            }
        });

        for (ImageView star : starList) {
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = starList.indexOf(v);
                    emptyStars(num);
                    fillStars(num);
                    rating = num+1;
                }
            });
        }
    }

    public void emptyStars(int num) {
        for (int i = rating-1; i > num; i--) {
            ImageView curr = starList.get(i);
            curr.setImageResource(R.drawable.star_empty);
        }
    }

    public void fillStars(int num) {
        for (int i = num; i > rating-1; i--) {
            ImageView curr = starList.get(i);
            curr.setImageResource(R.drawable.star_full);
        }
    }
}
