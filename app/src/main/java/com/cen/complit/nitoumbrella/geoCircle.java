package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Andrew on 1/28/2015.
 */
public class geoCircle extends  Fragment {
    Double myLat, myLong, radius;
    View rootview;

    private Boolean affHuman, affZombie, affOZ;

    private String TAG_TITLE = "title";
    private String TAG_END = "endDate";
    private String TAG_SUCCESS = "success";
    private String TAG_BODY = "body";
    private String TAG_ROLEID = "roleId";
    private String TAG_MISSIONDATA = "missionData";
    public String url = "http://hvz.sabaduy.com/api/" + ServiceHandler.APIKEY + "/game";
    public String passurl = "http://hvz.jrdbnntt.com/api/" + ServiceHandler.APIKEY + "/map/create/geofence";

    private String id,
            roleId = "5",
            title,
            mygameId,
            mymissionId;

    private String hMod, zMod, ozMod;

    private boolean status;

    private JSONArray missions;
    private ArrayList<String> missionsList = new ArrayList<String>();
    private ArrayList<String> myList = new ArrayList<String>();
    TextView gameText;

    Spinner mSpinner, colorSpinner;

    EditText modifHUM, modifZOM, modifOZ, desText;
    CheckBox cbHuman, cbOZ, cbZombie;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_geo_circle, container, false);
        radius  = this.getArguments().getDouble("radius");
        myLat = this.getArguments().getDouble("lat");
        myLong = this.getArguments().getDouble("long");
        myList = new ArrayList<String>();
        Button b = (Button) rootview.findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle myArgs = new Bundle();
                myArgs.putBoolean("modStatus", true);
                new SendCircle().execute();

                Toast.makeText(getActivity(), "GeoFence Added", Toast.LENGTH_LONG).show();

                Fragment myFrag = new modHub_Fragment();
                myFrag.setArguments(myArgs);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, myFrag)
                        .commit();
            }
        });

        affHuman = false;
        affOZ = false;
        affZombie = false;
//        hMod = 0.0;
//        zMod = 0.0;
//        ozMod = 0.0;


        gameText = (TextView) rootview.findViewById(R.id.gametext);

        colorSpinner = (Spinner) rootview.findViewById(R.id.colorSpin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.colors_array, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);

        mSpinner  = (Spinner) rootview.findViewById(R.id.missionSpin);

        modifHUM = (EditText) rootview.findViewById(R.id.hMod);
        modifZOM = (EditText) rootview.findViewById(R.id.zomMod);
        modifOZ = (EditText) rootview.findViewById(R.id.ozMod);
        desText = (EditText) rootview.findViewById(R.id.textView8);


        cbHuman = (CheckBox) rootview.findViewById(R.id.checkHumans);
        cbOZ = (CheckBox) rootview.findViewById(R.id.checkOZ);
        cbZombie = (CheckBox) rootview.findViewById(R.id.checkZombies);

        cbHuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbHuman.isChecked()) {
                    affHuman = false;
                    cbHuman.setChecked(true);
                }
                else {
                    cbHuman.setChecked(false);
                    affHuman = true;
                }


            }
        });

        cbZombie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbZombie.isChecked()) {
                    affZombie = false;
                    cbZombie.setChecked(true);
                }
                else {
                    cbZombie.setChecked(false);
                    affZombie = true;
                }


            }
        });

        cbOZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbOZ.isChecked()) {
                    affOZ = false;
                    cbOZ.setChecked(true);
                }
                else {
                    cbOZ.setChecked(false);
                    affOZ = true;
                }


            }
        });


        new GetGame().execute();
        return rootview;
    }
    private class GetGame extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("roleId", roleId));

            ServiceHandler sh = new ServiceHandler();

            //Make request to url
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
            Log.d("GAME", jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    status = jsonObject.getBoolean(TAG_SUCCESS);
                    if (status) {
                        JSONObject body = jsonObject.getJSONObject(TAG_BODY);
                        missions = body.getJSONArray(TAG_MISSIONDATA);
                        JSONObject games = body.getJSONObject("gameData");
                        title = games.getString(TAG_TITLE);
                        mygameId = games.getString("gameId");
                        Log.d("myTITLE", title);
                        Log.d("WHADAFUCK", missions.toString());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get data from URL");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            gameText.setText(title);
            Log.d("INPOST", "THIS SUCKS");
            if (missions != null) {
                Log.d("JESUS", "CHRIST");
                // looping through all updates
                for (int i = 0; i < missions.length(); i++) {
                    Log.d("OK", "now this is justgetting stupid");
                    try {
                        Log.d("WORK", "for the love of God");
                        JSONObject data = missions.getJSONObject(i);

                        // adding each child node to HashMap key => value
                        myList.add(data.getString(TAG_TITLE));
                        Log.d("TITLES", data.getString(TAG_TITLE));

                        mymissionId = data.getString("missionId");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("JSONNNNN", "Fuuuuuuuuuuuu");
                    }
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


            /**
             * Updating parsed JSON data into ListView
             * */
            ArrayAdapter myAdapter = new ArrayAdapter(getActivity(),
                    R.layout.custom_spinner, myList);

            mSpinner.setAdapter(myAdapter);
        }
    }

    private class SendCircle extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            //create service handler
            ServiceHandler sh = new ServiceHandler();
            String myHuman, myOZ, myZombie;

            if (cbHuman.isChecked())
                myHuman = modifHUM.getText().toString();
            else
                myHuman = "0";
            if (cbZombie.isChecked())
                myZombie = modifZOM.getText().toString();
            else
                myZombie = "0";
            if (cbOZ.isChecked())
                myOZ = modifOZ.getText().toString();
            else
                myOZ = "0";

            String mycolor = colorSpinner.getSelectedItem().toString();

            if (mycolor.matches("Pale Blue"))
                mycolor = "0x9CBEBD";
            else if (mycolor.matches("Brown"))
                mycolor = "2E2B21";
            else if (mycolor.matches("White"))
                mycolor = "0xFFFFFF";
            else
                mycolor = "0xC1272D";


            params.add(new BasicNameValuePair("gameId", mygameId));
            params.add(new BasicNameValuePair("missionId", mymissionId));
            params.add(new BasicNameValuePair("human", myHuman));
            params.add(new BasicNameValuePair("zombie", myZombie));
            params.add(new BasicNameValuePair("oz", myOZ));
            params.add(new BasicNameValuePair("color", mycolor));
            params.add(new BasicNameValuePair("latitude", myLat.toString()));
            params.add(new BasicNameValuePair("longitude", myLong.toString()));
            params.add(new BasicNameValuePair("radius", radius.toString()));
            params.add(new BasicNameValuePair("description", desText.getText().toString()));



            //Make request to url

            String jsonStr = sh.makeServiceCall(passurl, ServiceHandler.POST, params);

            //Log.d("DATA", jsonStr);

            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                status = jsonObject.getBoolean(TAG_SUCCESS);
                if(status) {
                    JSONObject dataBody = jsonObject.getJSONObject(TAG_BODY);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}