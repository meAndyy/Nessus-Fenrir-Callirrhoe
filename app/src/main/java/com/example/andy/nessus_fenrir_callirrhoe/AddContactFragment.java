package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddContactFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_add_contact, container, false);

        ctext = (EditText) v.findViewById(R.id.ctext);
        crtebtn = (Button) v.findViewById(R.id.crtebtn);
        addbtn = (Button) v.findViewById(R.id.addbtn);
        grpbtn = (Button) v.findViewById(R.id.grpbtn);
        ntext = (EditText) v.findViewById(R.id.ntext);
        viewgrp = (TextView) v.findViewById(R.id.viewgrp);
        list = (ListView) v.findViewById(R.id.list);
        contacts = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, contacts);
        list.setAdapter(adapter);

        grpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupname = ntext.getText().toString();
                if (!groupname.isEmpty() && groupname != null) {
                    viewgrp.setText(groupname);

                }
                else {
                    Toast.makeText(getActivity(),"Its empty!",Toast.LENGTH_SHORT).show();;
                }
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ctext.getText().toString().trim();
                if (!email.isEmpty()&& !email.contains(" ") ){
                    contacts.add(email);
                    ctext.setText("");
                }

                else {
                    Toast.makeText(getActivity(),"Its empty!",Toast.LENGTH_SHORT).show();;
                }

            }
        });

        crtebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String groupname = viewgrp.getText().toString().trim();
                if( !contacts.isEmpty() && !groupname.contains(" ")) {
                    final AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                    adb.setTitle("Create Group");
                    adb.setMessage("Are you sure you want to create this group? Doing so will overwrite any other group with the same name.");
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                                DatabaseController dbc = new DatabaseController();
                                dbc.addContacts(groupname, contacts);
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                Toast.makeText(getActivity(), groupname + " has been added to your contacts.", Toast.LENGTH_SHORT).show();
                                ctext.setText("");
                                ntext.setText("");
                                viewgrp.setText("");
                                contacts.clear();

                        }});
                    adb.show();
                }
                else{
                    Toast.makeText(getActivity(),"Bad group, check name for spaces.", Toast.LENGTH_LONG).show();
                }

                }

            }

        );

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
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

        return v;
    }

}

