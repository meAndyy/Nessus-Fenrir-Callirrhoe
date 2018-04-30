package com.example.andy.nessus_fenrir_callirrhoe;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.controllers.DatabaseController;
import com.example.controllers.NFCmanager;
import com.example.controllers.RequestAPI;
import com.example.fragments.AddContactFragment;
import com.example.fragments.LogFragment;
import com.example.fragments.MainFragment;
import com.example.models.LogHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.support.design.widget.TabLayout;

import com.example.adapters.TabAdapter;


public class NFCActivity extends AppCompatActivity implements MainFragment.OnDataPass {


    @Override
    public void onDataPass(HashMap<String, List<String>> data) {

        sendlist = toList(data);
    }
    protected NdefMessage message = null;
    protected NFCmanager nfcMger;
    Tag myTag;
    public Toolbar toolbar;
    String[] sendlist;
    String usremail;
    String uid;
    FirebaseUser currentFirebaseUser;
    String msgbdy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        findPref();
        nfcMger = new NFCmanager(this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new LogFragment())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

       ViewPager pager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        setSupportActionBar(toolbar);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        sendlist = new  String[10];
        initRecord();
       pager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_name:

                final FirebaseAuth auth = FirebaseAuth.getInstance();

                if(auth.getCurrentUser() != null){
                    auth.signOut();
                    startActivity(new Intent(NFCActivity.this, LoginActivity.class));
                    finish();
                }

                break;

            case R.id.action_message:
                startActivity(new Intent(NFCActivity.this, ChangeMessageActivity.class));
                break;

            case R.id.action_add:
                startActivity(new Intent(NFCActivity.this, AddContactFragment.class));
                finish();
                break;

            case R.id.action_write:
                startActivity(new Intent(NFCActivity.this, WriteTagActivity.class));
                finish();
                break;

            case R.id.action_read:
                startActivity(new Intent(NFCActivity.this, ReadActivty.class));
                finish();
        }
        return true;
    }

    protected void onResume() {
        super.onResume();
        String intentmsg = getIntent().getStringExtra("MSG_BDY");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("msg_bdy",intentmsg);
        editor.apply();

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
        if(auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage[] m = nfcMger.readFromIntent(intent);
            String s = nfcMger.buildTagViews(m);

            if(s.equals(uid) || s.equals("APOMDEFAULTVALUE")){

                Toast.makeText(this, "Sending..", Toast.LENGTH_LONG).show();
                findPref();
                new apiThread().execute(sendlist);

            }

        else{
                Toast.makeText(this, "This is someone elses tag. You'll need their permission to over-ride it.", Toast.LENGTH_LONG).show();
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

        return currconlist.toArray(new String[currconlist.size()]);
        }

    private void initRecord(){
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser != null) {
            uid= currentFirebaseUser.getUid();
            usremail = currentFirebaseUser.getEmail();
            OneSignal.sendTag("UserID",usremail);
            String s  = currentFirebaseUser.getUid();
            try {
                message = nfcMger.createRecord(s);
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        else{
            startActivity(new Intent(NFCActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void findPref(){
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        msgbdy = pref.getString("msg_bdy"," ");
    }

    protected class apiThread extends AsyncTask<String,Integer,Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {

            DatabaseController dbc = new DatabaseController();
            for (int i = 0; i < params.length; i++) {
                String playerid = params[i];
                RequestAPI api = new RequestAPI();
                Boolean isSent = api.sendData(playerid,msgbdy);
                if (isSent) {
                    LogHolder logholder = new LogHolder(playerid, "Outbound");
                    dbc.createLogInDatabase(logholder);
                    return true;
                }
                else{
                    //Toast.makeText(NFCActivity.this, "Error sending try again", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            return true;
        }

    }

}
