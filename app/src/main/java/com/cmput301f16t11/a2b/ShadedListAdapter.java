package com.cmput301f16t11.a2b;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Extends ArrayAdapter and modifies view so every even numbered entry is lightly shaded in
 * This work, "ShadedListAdapter," contains a derivative of an answer to
 * "Android Alternate row Colors in ListView" by "Suraj Bajaj," a user on Stack Overflow.
 * It is used under CC-BY-SA by CMPUT301F16T11.
 * It is available here:
 * http://stackoverflow.com/questions/13109840/android-alternate-row-colors-in-listview
 */

// for reference:
//adapter = new ArrayAdapter<UserRequest>(this, android.R.layout.simple_list_item_1,
//        android.R.id.text1, this.requests);
public class ShadedListAdapter<A> extends ArrayAdapter<A> {
    public ShadedListAdapter(Context cntxt, @LayoutRes int resource, @IdRes int textRes,
                             @NonNull List objs) {
        super(cntxt, resource, textRes, objs);
    }

    @NonNull
    @Override
    public View getView(int pos, View v, ViewGroup vGroup) {
        View view = super.getView(pos, v, vGroup);
        if (pos % 2 == 0) {
            // if even, shade the background
            view.setBackgroundColor(Color.rgb(224, 224, 224));

        }
        return view;
    }
}
