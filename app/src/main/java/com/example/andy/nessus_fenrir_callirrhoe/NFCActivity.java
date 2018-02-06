package com.example.andy.nessus_fenrir_callirrhoe;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.design.widget.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;


public class NFCActivity extends AppCompatActivity implements MainFragment.OnDataPass {

    private NdefMessage message = null;

    private NFCmanager nfcMger;
    Tag myTag;
    private ViewPager pager;
    private TabAdapter adapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    String[] sendlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        nfcMger = new NFCmanager(this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        pager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        setSupportActionBar(toolbar);
        adapter = new TabAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        sendlist = new  String[5];


        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String usremail = currentFirebaseUser.getEmail();
        OneSignal.syncHashedEmail(usremail);
        OneSignal.sendTag("UserID",usremail);

        if(currentFirebaseUser != null) {
            String s  = currentFirebaseUser.getUid();
            try {
                message = nfcMger.createRecord(s);
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
    }

    protected void onResume() {
        super.onResume();

        try {
            nfcMger.verifyNFC();

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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage[] m = nfcMger.readFromIntent(intent);
            String s = nfcMger.buildTagViews(m);
            Toast.makeText(this, "Tag value is " + s, Toast.LENGTH_LONG).show();
           // RequestAPI api = new RequestAPI();

            for(int i = 0; i < sendlist.length;i++) {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+sendlist);

            }
            new apiThread().execute(sendlist);
            /*if (message != null) {
                nfcMger.writeTag(myTag, message);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Toast.makeText(this, "User Info Loaded", Toast.LENGTH_SHORT).show();
                check = false;
            } else {

            }*/
        }
        else{
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    public String[] toList(HashMap<String,List<String>> map){
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        String currcontact = pref.getString("curr_contact","DEFAULT");
        List<String> currconlist = new ArrayList<>();
        if (map != null){

            for(Map.Entry m:map.entrySet()) {
                if(m.getKey().equals(currcontact)){
                    currconlist = (List<String>) m.getValue();

                }
                else{
                    System.out.println("skip");
                }
            }

        }
        String[] countries = currconlist.toArray(new String[currconlist.size()]);

            return countries;
        }


    @Override
    public void onDataPass(HashMap<String, List<String>> data) {

         sendlist = toList(data);


    }

    private class apiThread extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < params.length; i++) {
                String playerid = params[i];
                System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ"+playerid);
                playerid.trim();
                RequestAPI api = new RequestAPI();
                api.sendData(playerid);
            }

            return "1";
        }
    }

}
