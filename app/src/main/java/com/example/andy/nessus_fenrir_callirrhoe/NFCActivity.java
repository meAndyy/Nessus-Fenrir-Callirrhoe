package com.example.andy.nessus_fenrir_callirrhoe;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.UnsupportedEncodingException;
import android.support.design.widget.TabLayout;


public class NFCActivity extends AppCompatActivity {

    public static final String N0_TAG = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_FAIL = "Error during writing, is the NFC tag close enough to your device?";

    private NdefMessage message = null;
    private NFCmanager nfcMger;
    Tag myTag;
    FirebaseAuth auth;
    private ViewPager pager;
    private TabAdapter adapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_nfc);
        nfcMger = new NFCmanager(this);

        pager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        setSupportActionBar(toolbar);
        adapter = new TabAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String s  = currentFirebaseUser.getUid();
        try {
            message = nfcMger.createRecord(s);
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

    }

    protected void onResume() {
        super.onResume();

        try {
            nfcMger.verifyNFC();
            nfcMger.enableDispatch();

            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
            IntentFilter[] intentFiltersArray = new IntentFilter[] {};
            String[][] techList = new String[][] { { android.nfc.tech.Ndef.class.getName() }, { android.nfc.tech.NdefFormatable.class.getName() } };
            NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
            nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
        }
        catch(NFCmanager.NFCNotSupported nfcnsup) {

        }
        catch(NFCmanager.NFCNotEnabled nfcnEn) {

        }

    }



   protected void onPause() {
        super.onPause();
        nfcMger.disableDispatch();
    }

    @Override
    public void onNewIntent(Intent intent) {

        myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage[] m = nfcMger.readFromIntent(intent);
       String s  = nfcMger.buildTagViews(m);

        if (message != null) {
            nfcMger.writeTag(myTag, message);
            Toast.makeText(this, "User Info Loaded", Toast.LENGTH_LONG).show();

        }
        else {

        }
    }
}
