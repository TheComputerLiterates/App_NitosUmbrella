package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
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

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Andrew on 1/28/2015.
 * Massively Improved by Aaron on 3/11/2015 - Map Working
 */
public class liveMap_Fragment extends Fragment{

    static final LatLng DODD = new LatLng(30.440094, -84.292619);
    static final LatLng LANDIS = new LatLng(30.440910, -84.294993);
    static final LatLng HTL = new LatLng(30.444358, -84.300877);
    static final LatLng HCB = new LatLng(30.443358, -84.297038);
    static final LatLng WELL = new LatLng(30.441756, -84.298838);
    static final LatLng STAD = new LatLng(30.439019, -84.30339);
    static final LatLng PSY = new LatLng(30.445164, -84.304849);
    static final LatLng SLC = new LatLng(30.441030, -84.299192);
    static final LatLng WESFOUNT = new LatLng(30.440733, -84.291374);
    static final LatLng RDGREEN = new LatLng(30.441532, -84.291975);
    static final LatLng SHORES = new LatLng(30.441287, -84.296277);
    static final LatLng INTEG = new LatLng(30.443850, -84.298045);
    static final LatLng UNION = new LatLng(30.444362, -84.297201);
    static final LatLng UNION2 = new LatLng(30.444362, -84.297204);

    static final LatLng GAZ = new LatLng(30.439862, -84.294429);


    private MapView mapView;
    private GoogleMap mymap;
    boolean centerInit;
    LatLng mylocation;


    private View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.live_map_layout, container, false);
        centerInit = false;
        mapView = (MapView) rootview.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mymap = mapView.getMap();

        mymap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //callouts
        mymap.addMarker(new MarkerOptions()
                .position(DODD)
                .title("Dodd"));
        mymap.addMarker(new MarkerOptions()
                .position(LANDIS)
                .title("Landis"));
        mymap.addMarker(new MarkerOptions()
                .position(HTL)
                .title("HTL"));
        mymap.addMarker(new MarkerOptions()
                .position(HCB)
                .title("HCB"));
        mymap.addMarker(new MarkerOptions()
                .position(WELL)
                .title("Wellness"));
        mymap.addMarker(new MarkerOptions()
                .position(STAD)
                .title("Stadium"));
        mymap.addMarker(new MarkerOptions()
                .position(PSY)
                .title("Psych"));
        mymap.addMarker(new MarkerOptions()
                .position(SLC)
                .title("SLC"));
        mymap.addMarker(new MarkerOptions()
                .position(WESFOUNT)
                .title("Wescott Fountain"));
        mymap.addMarker(new MarkerOptions()
                .position(RDGREEN)
                .title("Ruby Diamond Green"));
        mymap.addMarker(new MarkerOptions()
                .position(SHORES)
                .title("Shores"));
        mymap.addMarker(new MarkerOptions()
                .position(INTEG)
                .title("Integration"));
        mymap.addMarker(new MarkerOptions()
                .position(UNION)
                .title("Union"));
        mymap.addMarker(new MarkerOptions()
                .position(GAZ)
                .title("Gazebo"));

        mymap.addCircle(new CircleOptions()
                        .center(UNION)
                        .radius(50)
                        .fillColor(0x409CBEBD)
                        .strokeColor(0x2e2b21)
        );
        mymap.addCircle(new CircleOptions()
                        .center(LANDIS)
                        .radius(20)
                        .fillColor(0x40C1272D)
                        .strokeColor(0x2e2b21)
        );

        MapsInitializer.initialize(this.getActivity());

        mymap.setMyLocationEnabled(true);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LANDIS, 17);
        mymap.animateCamera(cameraUpdate);
        mymap.setOnMyLocationChangeListener(myLocationChangeListener);


        return rootview;
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if(mymap != null){
                if (!centerInit) {
                    mymap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17));
                    centerInit = true;
                }
                else {
                    mylocation = loc;
                }
            }
        }
    };

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
