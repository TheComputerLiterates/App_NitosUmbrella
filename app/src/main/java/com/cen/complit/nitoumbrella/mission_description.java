package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Andrew on 4/19/2015.
 */
public class mission_description extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.modchat_layout, container, false);
        Bundle bundle = getArguments();
        String durr = bundle.getString("description");
        TextView tv = (TextView)rootview.findViewById(R.id.textView);
        tv.setText(durr);

        Button hurr = (Button)rootview.findViewById(R.id.returnbutton);
        hurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                Fragment frag = new viewMission_Fragment();
                fm.beginTransaction().replace(R.id.container, frag).commit();
            }
        });



        return rootview;
    }



}
