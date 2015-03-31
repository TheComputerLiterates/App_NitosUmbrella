package com.cen.complit.nitoumbrella;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;


/**
 Created by Aaron on 3/31/15
 */
public class modHub_Fragment extends Fragment implements View.OnClickListener{
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final Boolean isAdmin = this.getArguments().getBoolean("modStatus");
        rootview = inflater.inflate(R.layout.fragment_mod_hub, container, false);
        final Switch mySwitch = (Switch) rootview.findViewById(R.id.rekt_switch);
        Button mg = (Button) rootview.findViewById(R.id.mg_button);
        Button mm = (Button) rootview.findViewById(R.id.mm_button);
        Button gf = (Button) rootview.findViewById(R.id.gf_button);
        Button us = (Button) rootview.findViewById(R.id.u_button);
        Button mc = (Button) rootview.findViewById(R.id.mc_button);
        final TextView mySarc = (TextView) rootview.findViewById(R.id.sarcText);

        mySwitch.setTextOn("Mod");
        mySwitch.setTextOff("Scrub");
        mySwitch.setChecked(isAdmin);

        if(!isAdmin)
        {
            mySarc.setText("You don't look like a Mod to me, pal.");
            mg.setVisibility(View.GONE);
            mm.setVisibility(View.GONE);
            gf.setVisibility(View.GONE);
            us.setVisibility(View.GONE);
            mc.setVisibility(View.GONE);
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isAdmin)
                {
                    mySarc.setText("Are you leaving me?");
//                    try {
//                        wait(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    mySwitch.setChecked(true);
                    mySarc.setText("I won't let you give up.");
                }
                else
                {
//                    try {
//                        wait(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    mySarc.setText("Nice try, but no");
                    mySwitch.setChecked(false);
                }
            }
        });

        mg.setOnClickListener(this);
        mm.setOnClickListener(this);
        gf.setOnClickListener(this);
        us.setOnClickListener(this);
        mc.setOnClickListener(this);

        return rootview;

    }

    @Override
    public void onClick(View view) {
        Intent i;
        FragmentManager fm = getFragmentManager();
        Fragment myFr = null;
        switch (view.getId()){
            case R.id.mg_button:
                 myFr = new manageGames_Fragment();
                 break;
            case R.id.mm_button:
                myFr = new manageMissions_Fragment();
                break;
            case R.id.gf_button:
                myFr = new geofence_Fragment();
                break;
            case R.id.u_button:
                myFr = new users_Fragment();
                break;
            case R.id.mc_button:
                myFr = new modChat_Fragment();
                break;
        }
        fm.beginTransaction().replace(R.id.container, myFr).commit();
    }
}
