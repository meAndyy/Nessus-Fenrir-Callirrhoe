package com.example.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controllers.DatabaseController;
import com.example.andy.nessus_fenrir_callirrhoe.LoginActivity;
import com.example.andy.nessus_fenrir_callirrhoe.NFCActivity;
import com.example.andy.nessus_fenrir_callirrhoe.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AddContactFragment extends AppCompatActivity {

    EditText ctext, ntext;
    TextView viewgrp;
    Button addbtn, grpbtn, crtebtn;
    ListView list;
    ArrayList<String> contacts;
    ArrayAdapter<String> adapter;

    public AddContactFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_contact);
        ctext = (EditText) findViewById(R.id.ctext);
        crtebtn = (Button) findViewById(R.id.crtebtn);
        addbtn = (Button) findViewById(R.id.addbtn);
        grpbtn = (Button) findViewById(R.id.grpbtn);
        ntext = (EditText) findViewById(R.id.ntext);
        viewgrp = (TextView) findViewById(R.id.viewgrp);
        list = (ListView) findViewById(R.id.list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contacts = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
        list.setAdapter(adapter);

        grpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupname = ntext.getText().toString();
                if (!groupname.isEmpty() && groupname != null) {
                    viewgrp.setText(groupname);
                    hideKeyboard();


                }
                else {
                    Toast.makeText(getApplicationContext(),"Its empty!",Toast.LENGTH_SHORT).show();;
                }
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ctext.getText().toString().trim().toLowerCase();
                if (!email.isEmpty()&& !email.contains(" ") ){
                    contacts.add(email);
                    ctext.setText("");
                    hideKeyboard();
                }

                else {
                    Toast.makeText(getApplicationContext(),"Its empty!",Toast.LENGTH_SHORT).show();;
                }

            }
        });

        crtebtn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           final String groupname = viewgrp.getText().toString().trim().toLowerCase();
                                           if( !contacts.isEmpty() && !groupname.isEmpty()) {
                                               final AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                                               adb.setTitle("Create Group");
                                               adb.setMessage("Are you sure you want to create this group? Doing so will overwrite any other group with the same name.");
                                               adb.setNegativeButton("Cancel", null);
                                               adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                                   public void onClick(DialogInterface dialog, int which) {

                                                       DatabaseController dbc = new DatabaseController();
                                                       dbc.addContacts(groupname, contacts);
                                                       Toast.makeText(getApplicationContext(), groupname + " has been added to your contacts.", Toast.LENGTH_SHORT).show();
                                                       ctext.setText("");
                                                       ntext.setText("");
                                                       viewgrp.setText("");
                                                       contacts.clear();

                                                   }});
                                               adb.show();
                                           }
                                           else{
                                               Toast.makeText(getApplicationContext(),"Bad group, check name for spaces.", Toast.LENGTH_LONG).show();
                                           }

                                       }

                                   }

        );

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                adb.setTitle("Remove");
                adb.setMessage("Are you sure you want to remove this item from the group you are creating?");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        contacts.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }
                });
                adb.show();
                return true;

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
               /* AddContactFragment fragment = new AddContactFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.add_frag, fragment);
                transaction.commit();*/
                final FirebaseAuth auth = FirebaseAuth.getInstance();
                if(auth.getCurrentUser() != null){
                    auth.signOut();
                    startActivity(new Intent(AddContactFragment.this, LoginActivity.class));
                    finish();
                }
                break;

            case R.id.action_back:
                    startActivity(new Intent(AddContactFragment.this, NFCActivity.class));
                    finish();

        }
        return true;
    }


    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

