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
public class games_Fragment extends Fragment{
    public String purl = ServiceHandler.URL + ServiceHandler.APIKEY + "/user/profile";
    public String url = ServiceHandler.URL + ServiceHandler.APIKEY + "/game";
    private String TAG_SUCCESS = "success";
    private String TAG_BODY = "body";
    private String TAG_ROLEID = "roleId";
    private String TAG_GAMEDATA = "gameData";
    private String TAG_TITLE = "title";
    private String TAG_DESCRIPTION = "description";
    private String TAG_START = "startDate";
    private String TAG_END = "endDate";

    private boolean status;

    private String id,
    roleId,
    title,
    start,
    end,
    description,
    logindata;

    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.games_layout, container, false);

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

        new GetRole().execute();
        //new GetGame().execute();

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
            //Log.d("GAME", jsonStr);

            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    status = jsonObject.getBoolean(TAG_SUCCESS);
                    if(status){
                        JSONObject body = jsonObject.getJSONObject(TAG_BODY);
                        JSONObject data = body.getJSONObject(TAG_GAMEDATA);
                        title = data.getString(TAG_TITLE);
                        description = data.getString(TAG_DESCRIPTION);
                        start = data.getString(TAG_START);
                        end = data.getString(TAG_END);
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
            TextView t = (TextView)rootview.findViewById(R.id.title);
            TextView des = (TextView)rootview.findViewById(R.id.textView);
            TextView star = (TextView)rootview.findViewById(R.id.editStart);
            TextView en = (TextView)rootview.findViewById(R.id.editEnd);


            t.setText(title);
            des.setText(description);
            star.setText(start);
            en.setText(end);
        }

    }


    private class GetRole extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //create service handler

            params.add(new BasicNameValuePair("userId", id));

            ServiceHandler sh = new ServiceHandler();

            //Make request to url
            String jsonStr = sh.makeServiceCall(purl, ServiceHandler.POST, params);
            //Log.d("PROFILE", jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    status = jsonObject.getBoolean(TAG_SUCCESS);
                    if(status){
                        JSONObject body = jsonObject.getJSONObject(TAG_BODY);
                        roleId = body.getString(TAG_ROLEID);
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
            new GetGame().execute();
        }
    }
}
