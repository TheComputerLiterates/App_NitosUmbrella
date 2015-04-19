package com.cen.complit.nitoumbrella;

import android.app.ActionBar;
import android.app.Fragment;
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

    private View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.geofence_layout, container, false);
        final LatLng LANDIS = new LatLng(30.440910, -84.294993);
        myRad = new EditText(getActivity());
        myRad.setHint("Radius");
        gSub = new Button(getActivity());
        gSub.setText("Add!");

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        getActivity().addContentView(gSub, lp);
        getActivity().addContentView(myRad, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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
                rad = 0.0;
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
                Intent newFence = new Intent(getActivity(), geoCircle.class);
                newFence.putExtra("radius", rad);
                newFence.putExtra("lat", argLat);
                newFence.putExtra("long", argLong);
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
