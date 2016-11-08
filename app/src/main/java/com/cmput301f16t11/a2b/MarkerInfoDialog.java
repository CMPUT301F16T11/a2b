package com.cmput301f16t11.a2b;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Wilky on 11/7/2016.
 */

public class MarkerInfoDialog extends DialogFragment {

    private View layout;
    private TextView riderText;
    private TextView startText;
    private TextView endText;
    private TextView fareText;
    private Button acceptButton;
    private Button cancelButton;

    private UserRequest req;

    /**
     * Static creator method that assigns UserRequest to dialog
     *
     * @param req : UserRequest
     * @return dialog : RideCompleteDialog
     */
    public static MarkerInfoDialog newInstance(UserRequest req) {
        MarkerInfoDialog dialog = new MarkerInfoDialog();

        Bundle args = new Bundle();
        args.putParcelable("req", req);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the ride info
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Set the view & buttons
        builder.setView(inflater.inflate(R.layout.dialog_marker_info, null));

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.dialog_marker_info, container, false);

        // Assign views, get request and set views
        assignViews();
        req = getArguments().getParcelable("req");
        setViews();

        return layout;
    }

    /**
     * Find all views from layout view
     */
    public void assignViews() {
        riderText = (TextView)layout.findViewById(R.id.dialog_requestInfo_riderText);
        startText = (TextView)layout.findViewById(R.id.dialog_requestInfo_startText);
        endText = (TextView)layout.findViewById(R.id.dialog_requestInfo_endText);
        fareText = (TextView)layout.findViewById(R.id.dialog_requestInfo_fareText);
        acceptButton = (Button)layout.findViewById(R.id.dialog_markerInfo_accept);
        cancelButton = (Button)layout.findViewById(R.id.dialog_markerInfo_cancel);
    }


    /**
     * Set all TextViews
     * Assign onClickListeners
     */
    public void setViews() {
        // Attempt to get start/end addresses from LatLng
        Geocoder geocoder = new Geocoder(getActivity());
        String startAddress = "";
        String endAddress = "";
        try {
            List<Address> start = geocoder.getFromLocation(req.getStartLocation().latitude, req.getStartLocation().longitude, 1);
            List<Address> end = geocoder.getFromLocation(req.getEndLocation().latitude, req.getEndLocation().longitude, 1);

            if (!start.isEmpty()) {
                startAddress = start.get(0).getAddressLine(0);
            }
            if (!end.isEmpty()) {
                endAddress = end.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            Log.i("Error", "Failed to get addresses");
            e.printStackTrace();
        }

        // Set the textViews
        riderText.setText(req.getRider());
        startText.setText(startAddress);
        endText.setText(endAddress);
        fareText.setText(req.getFare().toString());

        // Set the button click listeners
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Ride accepting stuff
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the dialog when canceled
                MarkerInfoDialog.this.getDialog().cancel();
            }
        });

    }

}
