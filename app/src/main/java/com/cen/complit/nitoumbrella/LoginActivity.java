package com.cen.complit.nitoumbrella;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {

    private static String TAG_SUCCESS = "success";
    private static String TAG_BODY = "body";
    private static String TAG_USERID = "userId";
    private static String TAG_ROLEID = "roleId";

    private boolean status;
    private String myusername;
    private String mypassword;
    private String userId;
    private String roleId;
    public String url = ServiceHandler.URL + ServiceHandler.APIKEY + "/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //final EditText tvUser = (EditText) findViewById(R.id.username);
        //final EditText tvPass = (EditText) findViewById(R.id.password);
        Button b = (Button) findViewById(R.id.login_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new Test().execute();

            }
        });
    }


    private class Test extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            EditText tvUser = (EditText) findViewById(R.id.username);
            EditText tvPass = (EditText) findViewById(R.id.password);
            myusername = tvUser.getText().toString();
            mypassword = tvPass.getText().toString();


            //create service handler
            ServiceHandler sh = new ServiceHandler();



            params.add(new BasicNameValuePair("email", myusername));
            //Log.d("USER", myusername);
            params.add(new BasicNameValuePair("password", mypassword));
            //Log.d("PASS", mypassword);
            //Make request to url

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);

            //Log.d("DATA", jsonStr);

            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                status = jsonObject.getBoolean(TAG_SUCCESS);
                if(status) {
                    JSONObject dataBody = jsonObject.getJSONObject(TAG_BODY);
                    userId = dataBody.getString(TAG_USERID);
                    roleId = dataBody.getString(TAG_ROLEID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            EditText tvUser = (EditText) findViewById(R.id.username);
            EditText tvPass = (EditText) findViewById(R.id.password);
            FileOutputStream outputStream;
            //Update stuff
            if(status) {
                String filename = "session";

                String mylogin = myusername + ";" + mypassword + ";" + userId + ";" + roleId;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(mylogin.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent leggo = new Intent(LoginActivity.this, MainActivity.class);
                leggo.putExtra("rank", true);
                startActivity(leggo);
            }
            else{
                tvUser.setText("");
                tvPass.setText("");
                Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG).show();
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
