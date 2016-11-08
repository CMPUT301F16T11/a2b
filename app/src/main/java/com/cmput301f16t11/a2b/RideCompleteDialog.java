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
import android.widget.TextView;

/**
 * Created by Wilky on 11/7/2016.
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

    private UserRequest req;

    /**
     * Static creator method that assigns UserRequest to dialog
     *
     * @param req : UserRequest
     * @return dialog : RideCompleteDialog
     */
    public static RideCompleteDialog newInstance(UserRequest req) {
        RideCompleteDialog dialog = new RideCompleteDialog();

        Bundle args = new Bundle();
        args.putString("rider", req.getConfirmedDriver().getName().toString());
        args.putString("driver", req.getRider().getName().toString());
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
        builder.setView(inflater.inflate(R.layout.dialog_ride_complete, null));

        return builder.create();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.dialog_ride_complete, container, false);

        // Assign views, get request and set views
        assignViews();
        req = getArguments().getParcelable("req");
        req.getConfirmedDriver().setName(getArguments().getString("driver"));
        req.getRider().setName(getArguments().getString("rider"));
        setViews();

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
    }

    /**
     *  Assigns TextViews values based on req
     *
     *  Sets okButton click listener
     */
    public void setViews() {
        driver.setText(req.getConfirmedDriver().getName());
        rider.setText(req.getRider().getName());
        pickup.setText(req.getStartLocation().toString());
        dropoff.setText(req.getEndLocation().toString());
        fare.setText(req.getFare().toString());

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RideCompleteDialog.this.getDialog().dismiss();
            }
        });
    }
}
