package com.cen.complit.nitoumbrella;

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import android.location.Location;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class gpsService extends Service {

    public LocationListener myLL;
    public LocationManager myLM;
    private static Timer timer;


    public static final String
            ACTION_LOCATION_BROADCAST = gpsService.class.getName() + "LocationBroadcast",
            EXTRA_LATITUDE = "extra_latitude",
            EXTRA_LONGITUDE = "extra_longitude",
            url = "http://hvz.jrdbnntt.com/api/" + ServiceHandler.APIKEY + "/map/userGeopointCreate";

    private static final int
            MIN_TIME = 2000,
            MIN_DISTANCE = 1;

    private String myID = "";
    private String myLat;
    private String myLong;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "GPS Service Started", Toast.LENGTH_LONG).show();
        timer = new Timer();


        myLL = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                sendBroadcastMessage(location);
                new SendGPS().execute();
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
        myLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sendBroadcastMessage(myLM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        myLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, myLL);
    }

    private void sendBroadcastMessage(Location location) {
        if (location != null) {
            double getLat = location.getLatitude();
            double getLong = location.getLongitude();
            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(EXTRA_LATITUDE, getLat);
            myLat = Double.toString(getLat);
            intent.putExtra(EXTRA_LONGITUDE, getLong);
            myLong = Double.toString(getLong);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class myTimer extends TimerTask
    {
        public void run()
        {
            new SendGPS().execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "GPS Service Ended", Toast.LENGTH_LONG).show();
        myLM.removeUpdates(myLL);
        timer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        super.onStartCommand(intent, flags, startID);

        myID = intent.getStringExtra("myid");

        timer.scheduleAtFixedRate(new myTimer(), 0, 5000);

        return START_REDELIVER_INTENT;
    }

    private class SendGPS extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();


            //create service handler
            ServiceHandler sh = new ServiceHandler();


            params.add(new BasicNameValuePair("userId", myID));
            params.add(new BasicNameValuePair("latitude", myLat));
            params.add(new BasicNameValuePair("longitude", myLong));


            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
            Log.d("TAG", "GPS sent...");

            Log.d("DATA", jsonStr);


            return null;
        }
    }
}
