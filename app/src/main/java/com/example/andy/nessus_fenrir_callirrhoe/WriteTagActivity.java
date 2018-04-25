package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.UnsupportedEncodingException;

public class WriteTagActivity extends NFCActivity {

    RadioGroup rgrp;
    SharedPreferences.Editor editor;
    TextView txt;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt = (TextView)findViewById(R.id.txt);
        rgrp =(RadioGroup)findViewById(R.id.rgrp);
        final  SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        int index = sharedPref.getInt("write_mode", 1);
        RadioButton rb = (RadioButton)rgrp.getChildAt(index);
        rb.setChecked(true);
        createNewRecord(index);
        rgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {

             createNewRecord(checkedId);
          }
      });


        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usremail = currentFirebaseUser.getEmail();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_name:

                final FirebaseAuth auth = FirebaseAuth.getInstance();
                if(auth.getCurrentUser() != null){
                    auth.signOut();
                    startActivity(new Intent(WriteTagActivity.this, LoginActivity.class));
                    finish();
                }
                break;

            case R.id.action_back:
                startActivity(new Intent(WriteTagActivity.this, NFCActivity.class));
                finish();

        }
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {

            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage[] m = nfcMger.readFromIntent(intent);
            String s = nfcMger.buildTagViews(m);
            if(s.equals("") || s.equals("APOMDEFAULTVALUE") || s.equals(uid)){



                    nfcMger.writeTag(myTag, message);
                    Toast.makeText(this, "User Info Loaded", Toast.LENGTH_SHORT).show();
            }

            else {
                
                Toast.makeText(this, "This tag belongs to someone else. You'll need their permisssion to over-ride it", Toast.LENGTH_SHORT).show();

            }

        }

    }

    public void createNewRecord(int checkedId){
        if(checkedId == R.id.rb1){

            editor.putInt("write_mode",1);
            editor.commit();
            txt.setText(R.string.write);
            if(currentFirebaseUser != null) {
                String s  = "APOMDEFAULTVALUE";
                try {
                    message = nfcMger.createRecord(s);
                }
                catch(UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if(checkedId == R.id.rb2){
            editor.putInt("write_mode",3);
            editor.commit();
            txt.setText(R.string.safewrite);
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

        else  if(checkedId == R.id.rb3){
            editor.putInt("write_mode",5);
            editor.commit();
            txt.setText(R.string.erase);
            if(currentFirebaseUser != null) {
                String s  = "";
                try {
                    message = nfcMger.createRecord(s);
                }
                catch(UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

}
