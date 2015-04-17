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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrew on 1/28/2015.
 */


public class viewProfile_Fragment extends Fragment{

    private static String url = "http://fsuhvz.com/api/abc13ef/viewprofile/";
    private static String TAG_PROFILE = "profile";
    private static String TAG_FIRST_NAME = "first";
    private static String TAG_LAST_NAME = "second";
    private static String TAG_EMAIL = "email";
    private static String TAG_HVZID = "hvzid";
    private static String TAG_ROLE = "role";
    private static String name = "";
    private static String id = "";
    private static String role = "";
    private static String email = "";

    JSONArray profileInfo = null;
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.viewprofile_layout, container, false);

        //new GetProfileInfo().execute();

        return rootview;
    }

    private class GetProfileInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            //create service handler
            ServiceHandler sh = new ServiceHandler();

            //Make request to url
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    profileInfo = jsonObject.getJSONArray(TAG_PROFILE);

                    //Log.d("Derp", "made it here");

                    JSONObject c = profileInfo.getJSONObject(0);
                    name = c.getString(TAG_FIRST_NAME + "" + TAG_LAST_NAME);
                    id = c.getString(TAG_HVZID);
                    email = c.getString(TAG_EMAIL);
                    role = c.getString(TAG_ROLE);


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
            hid.setText(id);
            em.setText(email);
            ro.setText(role);
        }

    }

}
