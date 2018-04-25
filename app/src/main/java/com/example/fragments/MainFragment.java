package com.example.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controllers.DatabaseController;
import com.example.andy.nessus_fenrir_callirrhoe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import com.example.adapters.ExpandAdapter;


public class MainFragment extends Fragment {

    public interface OnDataPass {
        void onDataPass( HashMap<String, List<String>> data);
    }

    ExpandableListView list;
    ExpandableListAdapter adapter;
    HashMap<String, List<String>> result = new HashMap<>();
    TextView txtv;
    DatabaseController dbc;
    OnDataPass dataPasser;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View v = inflater.inflate(R.layout.fragment_main, container, false);

        dbc = new DatabaseController();
        String uid = dbc.getUid();
        txtv =(TextView)v.findViewById(R.id.txtv);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String s = sharedPref.getString("curr_contact","DEFAULT");
        txtv.setText("Current Contact: "+s);
        list = (ExpandableListView)v.findViewById(R.id.list);

       /* dbr.child("users").child(uid).child("contacts").addValueEventListener(new ValueEventListener() */

       final  DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("contacts");
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                       ArrayList<String> headers =  collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                        adapter = new ExpandAdapter(v.getContext(),headers, result);
                        list.setAdapter(adapter);
                        if (result != null){
                            passData(result);
                        }
                        else {
                            System.out.println("fffffffffffffffffffffffuuuuuuuuuuuuuuuuuuuuuuuu");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }

                });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int itemType = ExpandableListView.getPackedPositionType(id);

                if ( itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    String curr = result.keySet().toArray()[position].toString();
                    dbc.deleteData(curr);
                    Toast.makeText(getActivity(), "The "+curr+ " group has been deleted.", Toast.LENGTH_LONG).show();
                    return true;

                } else if(itemType ==  ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

                    return false;

                } else {

                    return false;
                }

            }
        });


        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String curr = (String)adapter.getGroup(groupPosition);//result.keySet().toArray()[groupPosition].toString();

                final AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                adb.setTitle("Set Up");
                adb.setMessage("Would you like to make "+curr+" your current contact group?");
                adb.setNegativeButton("No Thanks", null);
                adb.setPositiveButton("Sure", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        txtv.setText("Current Contact: "+curr);
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("curr_contact",curr);
                        editor.apply();
                        String s = sharedPref.getString("curr_contact","DEFAULT");
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }});
                adb.show();
                return true;
            }
        });



        return v;
    }

    public ArrayList<String> collectPhoneNumbers(Map<String,Object> users) {

        int i = 0;
        ArrayList<String> headers = new ArrayList<String>();
        if(users != null) {
            for (Map.Entry<String, Object> entry : users.entrySet()) {

                ArrayList<String> addresses = new ArrayList<String>();
                String s = entry.getKey();
                String x = entry.getValue().toString();
                System.out.println("********"+x);
                StringTokenizer tokenizer = new StringTokenizer(x, ",");
                while (tokenizer.hasMoreTokens()) {
                    String y = tokenizer.nextToken();
                    y = y.substring(y.indexOf("=") + 1);
                    y = y.replace("}", "");
                    addresses.add(y);
                }
                headers.add(s);
                result.put(headers.get(i), addresses);// HashMap to be delivered to expandable ListView
                i++;

            }
        }
        i = 0;
        return headers;

    }

    public void passData( HashMap<String, List<String>> data) {
        dataPasser.onDataPass(data);
    }

}
