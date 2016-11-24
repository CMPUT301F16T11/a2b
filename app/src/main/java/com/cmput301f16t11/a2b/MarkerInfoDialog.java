package com.cmput301f16t11.a2b;

import android.app.Dialog;
import android.app.DialogFragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Dialog with marker details with the ability to create new request
 */
public class MarkerInfoDialog extends DialogFragment {

    private TextView riderText;
    private TextView startText;
    private TextView endText;
    private TextView fareText;
    private TextView description;
    private Button acceptButton;
    private Button cancelButton;

    private UserRequest req;
    private String riderName;

    /**
     * Static creator method that assigns UserRequest to dialog
     *
     * @param req : UserRequest
     * @return dialog : RideCompleteDialog
     */
    public static MarkerInfoDialog newInstance(UserRequest req) {
        MarkerInfoDialog dialog = new MarkerInfoDialog();

        Bundle args = new Bundle();
        String riderId = req.getRiderID();
        User user = UserController.getUserFromId(riderId);
        args.putString("rider", user.getName());
        args.putParcelable("req", req);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the ride info
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate the parent view
        View parent = inflater.inflate(R.layout.dialog_marker_info, null);
        builder.setView(parent);

        // Assign views, get request and set views
        assignViews(parent);
        req = getArguments().getParcelable("req");
        riderName = getArguments().getString("rider");
        setViews();

        return builder.create();
    }

    /**
     * Find all views from layout view
     *
     * @param parent the parent view
     */
    public void assignViews(View parent) {
        riderText = (TextView)parent.findViewById(R.id.dialog_requestInfo_riderText);
        startText = (TextView)parent.findViewById(R.id.dialog_requestInfo_startText);
        endText = (TextView)parent.findViewById(R.id.dialog_requestInfo_endText);
        fareText = (TextView)parent.findViewById(R.id.dialog_requestInfo_fareText);
        description = (TextView)parent.findViewById(R.id.dialog_requestInfo_descripText);
        acceptButton = (Button)parent.findViewById(R.id.dialog_markerInfo_accept);
        cancelButton = (Button)parent.findViewById(R.id.dialog_markerInfo_cancel);
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
        riderText.setText(riderName);
        startText.setText(startAddress);
        endText.setText(endAddress);
        fareText.setText(req.getFare().toString());
        description.setText(req.getDescription());

        // Set the button click listeners
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElasticsearchRequestController.AddDriverAcceptanceToRequest addDriverTask = new ElasticsearchRequestController.AddDriverAcceptanceToRequest(getActivity());
                addDriverTask.execute(req.getId(), UserController.getUser().getId());
                DriverNotificationService.serviceHandler(req, getActivity());
                getDialog().dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the dialog when canceled
                getDialog().dismiss();
            }
        });

    }

}
