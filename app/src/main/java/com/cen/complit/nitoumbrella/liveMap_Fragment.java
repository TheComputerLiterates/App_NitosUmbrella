package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Andrew on 1/28/2015.
 */
public class liveMap_Fragment extends Fragment implements OnMapReadyCallback{
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.live_map_layout, container, false);
        return rootview;
    }

    MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    //Andrew, look into this issue if you have time. The fragment manager may be misplaced, possibly should go in main

    @Override
    public void onMapReady(GoogleMap map){

    }
}
