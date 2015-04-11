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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

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
    private static String url = "http://api.androidhive.info/contacts/";
    private static String TAG_CONTACTS = "contacts";
    private static String TAG_NAME = "name";

    JSONArray contact = null;
    public String name = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.menu1_layout, container, false);

        //new GetContacts().execute();

        return rootview;
    }

    /*
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            //create service handler
            ServiceHandler sh = new ServiceHandler();

            //Make request to url
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    contact = jsonObject.getJSONArray(TAG_CONTACTS);

                    //Log.d("Derp", "made it here");

                    JSONObject c = contact.getJSONObject(0);
                    name = c.getString(TAG_NAME);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                name = "it was null";
                Log.e("ServiceHandler", "Couldn't get data from URL");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            TextView tv = (TextView)rootview.findViewById(R.id.textView);
            //Update stuff
            tv.setText(name);
        }

    }*/
}
