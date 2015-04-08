package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Andrew on 1/28/2015.
 */
public class updates_Fragment extends Fragment{

    String[] dummyData = new String[] {"DumDum", "HerpDerp", "Dingus"};
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.updates_layout, container, false);
        return rootview;
    }

}
