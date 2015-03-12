package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Andrew on 1/28/2015.
 * Massively Improved by Aaron on 3/11/2015 - Map Working
 */
public class liveMap_Fragment extends Fragment{

    static final LatLng FSU = new LatLng(30.4406, -84.2914);
    private MapView mapView;
    private GoogleMap mymap;


    private View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.live_map_layout, container, false);

        mapView = (MapView) rootview.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mymap = mapView.getMap();

        mymap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng markerFSU = FSU;
        mymap.addMarker(new MarkerOptions()
            .position(markerFSU)
            .title("Florida State"));

        MapsInitializer.initialize(this.getActivity());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(FSU, 17);
        mymap.animateCamera(cameraUpdate);

        return rootview;
    }

    @Override
    public void onResume(){
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
