package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class MainFragment extends Fragment {

    ExpandableListView list;
    ExpandableListAdapter adapter;
    ArrayList<String> contacts;
    HashMap<String, List<String>> result = new HashMap<>();
    TextView txtv;

    public MainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View v = inflater.inflate(R.layout.fragment_main, container, false);

        DatabaseController dbc = new DatabaseController();
        DatabaseReference dbr = dbc.getDatabase();
        String uid = dbc.getUid();
        txtv =(TextView)v.findViewById(R.id.txtv);
        txtv.setText(uid);
        list = (ExpandableListView)v.findViewById(R.id.list);
       /* dbr.child("users").child(uid).child("contacts").addValueEventListener(new ValueEventListener() */


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("contacts");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                       ArrayList<String> headers =  collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                        adapter = new ExpandAdapter(v.getContext(),headers, result);
                        list.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });



        return v;
    }

    private ArrayList<String> collectPhoneNumbers(Map<String,Object> users) {


        ArrayList<String> holder = new ArrayList<String>();
        int i = 0;
        ArrayList<String> headers = new ArrayList<String>();
        if(users != null) {
            for (Map.Entry<String, Object> entry : users.entrySet()) {

                ArrayList<String> addresses = new ArrayList<String>();
                String s = entry.getKey();
                String x = entry.getValue().toString();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!" +s +""+ x);
                StringTokenizer tokenizer = new StringTokenizer(x, ",");
                while (tokenizer.hasMoreTokens()) {
                    String y = tokenizer.nextToken();
                    y = y.substring(y.indexOf("=") + 1);
                    y = y.replace("}", "");
                    addresses.add(y);
                }

                headers.add(s);
                result.put(headers.get(i), addresses);
                i++;

            }
            /*for (int j = 0; j < addresses.size(); j++) {
                System.out.println(addresses.get(j));

            }*/
        }
        else{
            Log.d("Mytag","Help");
        }
        i = 0;
        return headers;

    }

}
