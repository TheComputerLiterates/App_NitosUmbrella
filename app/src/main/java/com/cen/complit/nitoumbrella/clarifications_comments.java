package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 4/20/2015.
 */
public class clarifications_comments extends Fragment {
    View rootview;
    Bundle chungis = new Bundle();
    public String url = "http://hvz.sabaduy.com/api/" + ServiceHandler.APIKEY + "/user/cRequestView_commentGet";
    public String curl = "http://hvz.sabaduy.com/api/" + ServiceHandler.APIKEY + "/user/cRequestView_commentCreate";


    final private String TAG_SUCCESS = "success";
    final private String TAG_BODY = "body";
    final private String TAG_USER = "userName";
    final private String TAG_TEXT = "text";
    final private String TAG_COMMENTS = "comments";

    private ArrayList<String> text = new ArrayList<String>();
    private ArrayList<String> user = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> comList = new ArrayList<HashMap<String, String>>();


    private String id,
            logindata;
    private boolean status;
    private JSONArray comments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.comments_layout, container, false);


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


        new GetComments().execute();
        Button hurr = (Button)rootview.findViewById(R.id.backbutton);
        hurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                Fragment frag = new clarifications_Fragment();
                fm.beginTransaction().replace(R.id.container, frag).commit();
            }
        });

        Button rep = (Button)rootview.findViewById(R.id.replybutton);
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ding = (EditText)rootview.findViewById(R.id.reply);
                if(!ding.getText().toString().equals("")) {
                    new PostComment().execute();

                    FragmentManager fm = getFragmentManager();
                    Fragment frag = new clarifications_comments();
                    frag.setArguments(chungis);
                    fm.beginTransaction().replace(R.id.container, frag).commit();
                }
            }
        });

        return rootview;
    }

    private class GetComments extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //create service handler
            chungis = getArguments();
            String id = chungis.getString("durr");
            params.add(new BasicNameValuePair("crId", id));

            ServiceHandler sh = new ServiceHandler();

            //Make request to url
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
            Log.d("COMMENTS", jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject body = jsonObject.getJSONObject(TAG_BODY);
                    comments = body.getJSONArray(TAG_COMMENTS);


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

            final ListView listview = (ListView) rootview.findViewById(R.id.clarlist);

            if (comments != null) {

                // looping through all updates
                for (int i = 0; i < comments.length(); i++) {
                    try {
                        JSONObject data = comments.getJSONObject(i);
                        // tmp hashmap for single update
                        HashMap<String, String> com = new HashMap<String, String>();

                        Log.d("CLAR", comments.getString(i));
                        // adding each child node to HashMap key => value
                        com.put(TAG_USER, data.getString(TAG_USER) + " said:");
                        com.put(TAG_TEXT, data.getString(TAG_TEXT));

                        text.add(data.getString(TAG_TEXT));
                        user.add(data.getString(TAG_USER));

                        comList.add(com);

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
            ListAdapter adapter = new SimpleAdapter(getActivity(), comList,
                    R.layout.comment_list_view, new String[] { TAG_USER, TAG_TEXT}, new int[] { R.id.firstLine, R.id.secondLine});

            listview.setAdapter(adapter);

        }

    }


    private class PostComment extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            chungis = getArguments();

            EditText dippy = (EditText)rootview.findViewById(R.id.reply);
            String sunuvabich = dippy.getText().toString();
            String dammit = chungis.getString("durr");
            params.add(new BasicNameValuePair("userId", id));
            params.add(new BasicNameValuePair("crId", dammit));
            params.add(new BasicNameValuePair("comment", sunuvabich));



            ServiceHandler sh = new ServiceHandler();

            //Make request to url
            String jsonStr = sh.makeServiceCall(curl, ServiceHandler.POST, params);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    status = jsonObject.getBoolean(TAG_SUCCESS);
                    if(status){
                       Log.d("WIN!", jsonStr);
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

        }

    }


}
