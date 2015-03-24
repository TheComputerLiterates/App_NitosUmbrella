package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;


/**
 * Created by Andrew on 1/28/2015.
 */
public class enableGPS_Fragment extends Fragment{
    View rootview;
    int gps_state;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.gps_layout, container, false);
        final TextView textView = (TextView) rootview.findViewById(R.id.locationView);

//        final Intent goGPS = new Intent(getActivity(), gpsService.class);
//
        final Button gps_on = (Button) rootview.findViewById(R.id.on_button);
        gps_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity().getApplicationContext(), "GPS Enabled", Toast.LENGTH_LONG).show();
                getActivity().startService(new Intent(getActivity(), gpsService.class));
            }
        });

        final Button gps_off = (Button) rootview.findViewById(R.id.off_button);
        gps_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().stopService(new Intent(getActivity(), gpsService.class));
                textView.setText("Awaiting GPS");
               // Toast.makeText(getActivity().getApplicationContext(), "GPS Disabled", Toast.LENGTH_LONG).show();
            }
        });


        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double latitude = intent.getDoubleExtra(gpsService.EXTRA_LATITUDE, 0);
                        double longitude = intent.getDoubleExtra(gpsService.EXTRA_LONGITUDE, 0);
                        textView.setText("Lat: " + latitude + ", Lng: " + longitude);
                    }
                }, new IntentFilter(gpsService.ACTION_LOCATION_BROADCAST)
        );
        return rootview;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onStop();
//    }
}



