package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class LogFragment extends Fragment implements OneSignal.NotificationReceivedHandler {

    ListView list;
    ArrayList<LogHolder> msgs;
   LogListAdapter adapter;
    DatabaseController dbc;

    public LogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_log, container, false);
        dbc = new DatabaseController();
        list = (ListView) v.findViewById(R.id.list);
        msgs = new ArrayList<>();
        adapter = new LogListAdapter( msgs,getActivity());
        list.setAdapter(adapter);
        return v;
    }

    @Override
    public void notificationReceived(OSNotification notification) {

        try {
            JSONObject obj = notification.payload.additionalData;
            String senderid = obj.getString("foo");
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx CRITICAL FAILURE");
            final LogHolder logholder = new LogHolder(senderid, "Inbound");
            dbc.createLogInDatabase(logholder);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx CRITICAL FAILURE");

        } finally {

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
        String uid = dbc.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("log").child(uid);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        msgs.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            LogHolder logHolder = child.getValue(LogHolder.class);
                            String date = logHolder.getDate();
                            System.out.println("£££££££££££££££££££££££ "+date);
                            msgs.add(logHolder);
                            System.out.println("£££££££££££££££££££££££ "+date);
                        }

                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
