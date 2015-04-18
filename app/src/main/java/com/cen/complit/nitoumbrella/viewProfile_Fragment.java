package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 1/28/2015.
 */


public class viewProfile_Fragment extends Fragment{

    public String url = "http://hvz.sabaduy.com/api/" + ServiceHandler.APIKEY + "/user/profile";
    private static String TAG_SUCCESS = "success";
    private static String TAG_BODY = "body";
    private static String TAG_FIRST = "firstName";
    private static String TAG_LAST = "lastName";
    private static String TAG_EMAIL = "email";
    private static String TAG_HVZID = "HVZID";
    private static String TAG_ROLE = "roleName";
    private boolean status = false;
    private String name,
    id,
    hvzid,
    role,
    email,
    first,
    last,
    logindata;

    JSONArray profileInfo = null;
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.viewprofile_layout, container, false);

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
                id = separated[2];

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        new GetProfileInfo().execute();

        return rootview;
    }

    private class GetProfileInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //create service handler

            params.add(new BasicNameValuePair("userId", id));

            ServiceHandler sh = new ServiceHandler();

            //Make request to url
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
            Log.d("PROFILE", jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    status = jsonObject.getBoolean(TAG_SUCCESS);
                    if(status){
                        JSONObject body = jsonObject.getJSONObject(TAG_BODY);
                        first = body.getString(TAG_FIRST);
                        last = body.getString(TAG_LAST);
                        name = first + " " + last;
                        email = body.getString(TAG_EMAIL);
                        hvzid = body.getString(TAG_HVZID);
                        role = body.getString(TAG_ROLE);
                    }
                    else{
                        name = "error";
                        email = "connecting";
                        hvzid = "to";
                        role = "server";
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
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            TextView pn = (TextView)rootview.findViewById(R.id.playername);
            TextView hid = (TextView)rootview.findViewById(R.id.pHvzid);
            TextView em = (TextView)rootview.findViewById(R.id.emailText);
            TextView ro = (TextView)rootview.findViewById(R.id.roleStuff);


            //Update stuff
            pn.setText(name);
            hid.setText(hvzid);
            em.setText(email);
            ro.setText(role);
        }

    }

}
