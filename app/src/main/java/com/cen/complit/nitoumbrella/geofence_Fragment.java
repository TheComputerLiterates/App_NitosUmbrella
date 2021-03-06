package com.cen.complit.nitoumbrella;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Andrew on 1/28/2015.
 */
public class geofence_Fragment extends Fragment {
    private MapView mapView;
    private GoogleMap mymap;
    EditText myRad;
    Button gSub;
    Double rad, argLat, argLong;

    Bundle mycircle;
    Fragment myFrag;

    private View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.geofence_layout, container, false);
        final LatLng LANDIS = new LatLng(30.440910, -84.294993);
        myRad = (EditText) rootview.findViewById(R.id.radText);
        myRad.setHint("Radius");
        gSub = (Button) rootview.findViewById(R.id.addCircle);
        gSub.setText("Add!");
        gSub.setBackgroundColor(getActivity().getResources().getColor(R.color.umbrella_red));
        myFrag = null;
        mycircle = new Bundle();
        argLat = 0.0;
        argLong = 0.0;


        mapView = (MapView) rootview.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mymap = mapView.getMap();
        mymap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        MapsInitializer.initialize(this.getActivity());
        mymap.getUiSettings().setCompassEnabled(false);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LANDIS, 17);
        mymap.animateCamera(cameraUpdate);

        mymap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mymap.clear();
                if (myRad.getText().toString().matches(""))
                    rad = 0.0;
                else
                    rad = Double.parseDouble(myRad.getText().toString());
                argLat = point.latitude;
                argLong = point.longitude;
                if (rad != 0.0) {
                    mymap.addCircle(new CircleOptions()
                                    .center(point)
                                    .radius(rad)
                                    .fillColor(0x40C1272D)
                                    .strokeColor(0x2e2b21)
                    );
                }
            }
        });

        gSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mycircle.putDouble("radius", rad);
                mycircle.putDouble("lat", argLat);
                mycircle.putDouble("long", argLong);
                myFrag = new geoCircle();
                myFrag.setArguments(mycircle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, myFrag)
                        .commit();
            }
        });

        return rootview;
    }

    @Override
    public void onResume(){
        mapView.onResume();
        super.onResume();
        myRad.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRad.setVisibility(View.GONE);
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
