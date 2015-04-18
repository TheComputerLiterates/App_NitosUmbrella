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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by Andrew on 1/28/2015.
 */
public class enableGPS_Fragment extends Fragment{
    static boolean gps_state = false;
    static String myText = "";
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.gps_layout, container, false);
        final TextView textView = (TextView) rootview.findViewById(R.id.locationView);

        String logindata;
        String parseID = "";
        final String argID;

        String filename = "session";
        File myFile = new File(getActivity().getFilesDir(), filename);
        if (myFile.exists()) {
            FileInputStream inputStream;
            try {
                inputStream = getActivity().openFileInput(filename);
                byte[] login_test = new byte[inputStream.available()];
                while (inputStream.read(login_test) != -1) {
                }
                logindata = new String(login_test);
                String[] separated = logindata.split(";");
                parseID = separated[2];

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        argID = parseID;

        if (!gps_state)
        {
            myText = "Awaiting GPS";
        }
        textView.setText(myText);

        final Button gps_on = (Button) rootview.findViewById(R.id.on_button);
        gps_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), gpsService.class);
                i.putExtra("myid", argID);
                getActivity().startService(i);
                gps_state = true;
            }
        });

        final Button gps_off = (Button) rootview.findViewById(R.id.off_button);
        gps_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().stopService(new Intent(getActivity(), gpsService.class));
                myText = "Awaiting GPS";
                textView.setText(myText);
                gps_state = false;
            }
        });


        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double latitude = intent.getDoubleExtra(gpsService.EXTRA_LATITUDE, 0);
                        double longitude = intent.getDoubleExtra(gpsService.EXTRA_LONGITUDE, 0);
                        myText = "Lat: " + latitude + ", Lng: " + longitude;
                        textView.setText(myText);
                    }
                }, new IntentFilter(gpsService.ACTION_LOCATION_BROADCAST)
        );
        return rootview;
    }
}



