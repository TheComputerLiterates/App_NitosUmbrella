package com.cen.complit.nitoumbrella;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

/**
 * Created by Andrew on 1/28/2015.
 */
public class logout_Fragment extends Fragment{
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        String filename = "session";
        File session = new File(getActivity().getFilesDir(), filename);
        session.delete();

        rootview = inflater.inflate(R.layout.activity_login, container, false);
        return rootview;

    }
}
