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
import android.widget.TextView;
import android.widget.Toast;

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
public class users_Fragment extends Fragment{
    public String url = "http://hvz.sabaduy.com/api/" + ServiceHandler.APIKEY + "/user/cRequestCreate_submit";

    private String id,
    logindata;
    private Boolean status;

    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.users_layout, container, false);

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



        Button hurr = (Button)rootview.findViewById(R.id.back);
        hurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                Fragment frag = new clarifications_Fragment();
                fm.beginTransaction().replace(R.id.container, frag).commit();
            }
        });

        Button durr = (Button)rootview.findViewById(R.id.submit);
        durr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PostClar().execute();
                FragmentManager fm = getFragmentManager();
                Fragment frag = new clarifications_Fragment();
                fm.beginTransaction().replace(R.id.container, frag).commit();
            }
        });

        return rootview;
    }


    private class PostClar extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            EditText sub = (EditText)rootview.findViewById(R.id.subedit);
            EditText des = (EditText)rootview.findViewById(R.id.descedit);
            EditText per = (EditText)rootview.findViewById(R.id.personedit);

            String subj = sub.getText().toString();
            String desc = des.getText().toString();
            String pers = per.getText().toString();


            //create service handler
            ServiceHandler sh = new ServiceHandler();
            String jsonStr;

            params.add(new BasicNameValuePair("userId", id));
            params.add(new BasicNameValuePair("subject", subj));
            params.add(new BasicNameValuePair("description", desc));

            if(pers.equals("Y") || pers.equals("y")){
                params.add(new BasicNameValuePair("personal", "true"));
                jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
            }
            else if(pers.equals("N") || pers.equals("n")){
                params.add(new BasicNameValuePair("personal", "false"));
                jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
            }
            else {
                jsonStr = null;
            }


            return null;
        }

    }
}
