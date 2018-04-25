package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_message);

        final EditText msgbdy =(EditText)findViewById(R.id.msgbdy);
        final Button btn =(Button)findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgbdy != null){
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("msg_bdy",msgbdy.getText().toString());
                    editor.apply();
                    String s = sharedPref.getString("curr_contact","DEFAULT");
                    Toast.makeText(ChangeMessageActivity.this, "Message Changed.", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(ChangeMessageActivity.this, "Its blank, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
