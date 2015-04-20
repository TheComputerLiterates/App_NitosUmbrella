package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 1/28/2015.
 */
public class menu1_Fragment extends Fragment{
    View rootview;

    // url to make request
    public String url = "http://hvz.sabaduy.com/api/" + ServiceHandler.APIKEY + "/user/profile";
    private static String TAG_SUCCESS = "success";
    private static String TAG_BODY = "body";
    private static String TAG_FIRST = "firstName";
    private static String TAG_LAST = "lastName";
    public boolean status;
    private String first;
    private String last;



    public String name = "";
    public String logindata;
    public String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.menu1_layout, container, false);

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

        new GetName().execute();

        return rootview;
    }


    private class GetName extends AsyncTask<Void, Void, Void> {
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
            TextView tv = (TextView)rootview.findViewById(R.id.textView);
            //Update stuff
            if(name.equals("")) {
                tv.setText("Error Connecting to Server");
            }
            else
                tv.setText("Logged in as: " + name);
        }

    }
}
