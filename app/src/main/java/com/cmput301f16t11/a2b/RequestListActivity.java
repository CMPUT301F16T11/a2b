package com.cmput301f16t11.a2b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RequestListActivity extends AppCompatActivity {
    /**
     * This work, "MainHabitActivity," contains a derivative
     * "Android - How to create clickable listview?" by "Delpes," a stackoverflow user,
     * used under CC-BY-SA by Joey-Michael Fallone.
     * (Available here:
     * http://stackoverflow.com/questions/13281197/android-how-to-create-clickable-listview)
     */
    private ArrayList<Request> requests;
    private ArrayAdapter<Request> adapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.requests = RequestController.tempFakeRequestList();
        this.populateRequestList();
    }

    private void populateRequestList() {
        listView = (ListView) findViewById(R.id.requestList);
        // make it click!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                //TODO: some code here that responds to clicking on request
                // perhaps a dialog?
            }
        });
        adapter = new ArrayAdapter<Request>(this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, this.requests);
        listView.setAdapter(adapter);
    }
}
