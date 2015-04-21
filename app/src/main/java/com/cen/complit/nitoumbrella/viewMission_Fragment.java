package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 1/28/2015.
 */
public class viewMission_Fragment extends Fragment {
    public String purl = ServiceHandler.URL + ServiceHandler.APIKEY + "/user/profile";
    public String url = ServiceHandler.URL + ServiceHandler.APIKEY + "/game";
    private String TAG_SUCCESS = "success";
    private String TAG_BODY = "body";
    private String TAG_ROLEID = "roleId";
    private String TAG_MISSIONDATA = "missionData";
    private String TAG_TITLE = "title";
    private String TAG_DESCRIPTION = "description";
    private String TAG_START = "startDate";
    private String TAG_END = "endDate";

    private boolean status;

    private JSONArray missions;
    private ArrayList<HashMap<String, String>> missionsList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> descriptions = new ArrayList<String>();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.mission_layout, container, false);

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
            final ListView listview = (ListView) rootview.findViewById(R.id.missionlist);

            if (missions != null) {

                // looping through all updates
                for (int i = 0; i < missions.length(); i++) {
                    try {
                        JSONObject data = missions.getJSONObject(i);
                        // tmp hashmap for single update
                        HashMap<String, String> mission = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        mission.put(TAG_TITLE, data.getString(TAG_TITLE));
                        mission.put(TAG_END, "Ends " + data.getString(TAG_END));

                        descriptions.add(data.getString(TAG_DESCRIPTION));
                        missionsList.add(mission);


                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(getActivity(), missionsList,
                    R.layout.list_view, new String[] { TAG_TITLE, TAG_END}, new int[] { R.id.firstLine, R.id.secondLine});

            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Bundle bundle = new Bundle();
                    bundle.putString("description", descriptions.get(i));
                    FragmentManager fm = getFragmentManager();
                    Fragment frag = new mission_description();
                    frag.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.container, frag).commit();


                }
            });


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
            Log.d("PROFILE", jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    status = jsonObject.getBoolean(TAG_SUCCESS);
                    if (status) {
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            new GetGame().execute();
        }
    }
}





