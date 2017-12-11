package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainFragment extends Fragment {

    TextView txt;

    public MainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        txt = (TextView)v.findViewById(R.id.txt);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String s = user.getEmail();
        txt.setText("Hello, \n"+s);
        return v;
    }

}
