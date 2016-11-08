package com.cmput301f16t11.a2b;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/**
 * Created by Wilky on 11/7/2016.
 */

public class MarkerInfoDialog extends DialogFragment {

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

        // Set the view & buttons
        builder.setView(inflater.inflate(R.layout.dialog_marker_info, null));

        return builder.create();
    }

}
