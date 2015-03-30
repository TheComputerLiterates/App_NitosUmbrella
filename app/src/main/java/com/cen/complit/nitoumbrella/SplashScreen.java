package com.cen.complit.nitoumbrella;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SplashScreen extends Activity {

    private static int TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            //show splash screen for 3 seconds
            //then launch main

            @Override
            public void run() {
                //if file exists and contains proper credentials, go to main
                boolean myflag = false;
                String myusername, mypassword, mylogin;
                String logindata;
                String filename = "session";
                File myFile = new File(getApplicationContext().getFilesDir(), filename);
                if (myFile.exists()) {
                    FileInputStream inputStream;
                    try {
                        inputStream = openFileInput(filename);
                        byte[] login_test = new byte[inputStream.available()];
                        while (inputStream.read(login_test) != -1) {}
                        logindata = new String(login_test);
                        String[] separated = logindata.split(";");
                        myusername = separated[0];
                        mypassword = separated[1];

                        if ((myusername.matches("admin")) && (mypassword.matches("gravelord")))
                        {
                            myflag = true;
                        }
                        else {
                            myflag = false;
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "FML", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {

                    }


                }
                //else go to Login
                if (myflag)
                {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }
                //this launches main when timer runs out


                finish();
            }
        }, TIME_OUT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
