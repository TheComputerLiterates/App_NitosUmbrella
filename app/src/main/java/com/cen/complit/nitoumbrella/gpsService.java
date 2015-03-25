package com.cen.complit.nitoumbrella;

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import android.location.Location;

public class gpsService extends Service {

    public LocationListener myLL;
    public LocationManager myLM;

    public static final String
            ACTION_LOCATION_BROADCAST = gpsService.class.getName() + "LocationBroadcast",
            EXTRA_LATITUDE = "extra_latitude",
            EXTRA_LONGITUDE = "extra_longitude";

    private static final int
            MIN_TIME = 2000,
            MIN_DISTANCE = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "GPS Service Started", Toast.LENGTH_LONG).show();

        myLL = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        sendBroadcastMessage(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
        myLM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        sendBroadcastMessage(myLM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        myLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, myLL);
    }

    private void sendBroadcastMessage(Location location) {
        if (location != null) {
            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(EXTRA_LATITUDE, location.getLatitude());
            intent.putExtra(EXTRA_LONGITUDE, location.getLongitude());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "GPS Service Ended", Toast.LENGTH_LONG).show();
        myLM.removeUpdates(myLL);
    }
}
