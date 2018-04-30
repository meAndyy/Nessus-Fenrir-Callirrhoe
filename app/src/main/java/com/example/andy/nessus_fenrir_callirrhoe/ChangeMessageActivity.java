package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ChangeMessageActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_message);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText msgbdy =(EditText)findViewById(R.id.msgbdy);
        final Button btn =(Button)findViewById(R.id.btn);
        final TextView txt = (TextView) findViewById(R.id.txt);
        final SharedPreferences pref = getPreferences(Context.MODE_APPEND);
        String pholder = pref.getString("msg_bdy"," ");
        if (!pholder.equals(" "))
        txt.setText("Message: "+pholder);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgbdy != null){
                    String s = msgbdy.getText().toString();
                    txt.setText("Message: "+s);
                    Toast.makeText(ChangeMessageActivity.this, "Message Changed.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), NFCActivity.class);
                    intent.putExtra("MSG_BDY", s);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("msg_bdy",s);
                    editor.apply();
                    startActivity(intent);
                }

                else{
                    Toast.makeText(ChangeMessageActivity.this, "Its blank, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
                    startActivity(new Intent(ChangeMessageActivity.this, LoginActivity.class));
                    finish();
                }
                break;

            case R.id.action_back:
                startActivity(new Intent(ChangeMessageActivity.this, NFCActivity.class));
                finish();

        }
        return true;
    }
}
