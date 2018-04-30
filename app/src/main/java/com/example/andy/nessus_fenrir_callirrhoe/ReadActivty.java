package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ReadActivty extends WriteTagActivity{

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt = (TextView)findViewById(R.id.toptxt);
    }

    @Override
    public void onNewIntent(Intent intent) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage[] m = nfcMger.readFromIntent(intent);
        String s = nfcMger.buildTagViews(m);
        txt.setText("Tag Value Is: "+s);

    }
}
