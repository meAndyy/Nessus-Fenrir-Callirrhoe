package com.example.andy.nessus_fenrir_callirrhoe;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;


public class RWFragment extends Fragment {

    private NdefMessage message = null;
    private NFCmanager nfcMger;
    Tag myTag;
    boolean check;

    TextView valueNfc;

    public RWFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_rw, container, false);
        nfcMger = new NFCmanager(getActivity());
        check = false;
        valueNfc = (TextView)v.findViewById(R.id.valueNfc);

        if (nfcMger.isCheck()){
            valueNfc.setText(nfcMger.getText());
        }

        return v;
    }



}
