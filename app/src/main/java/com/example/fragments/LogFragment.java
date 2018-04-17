package com.example.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.controllers.DatabaseController;
import com.example.controllers.GraphController;
import com.example.models.LogHolder;
import com.example.andy.nessus_fenrir_callirrhoe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

import com.example.adapters.LogListAdapter;

public class LogFragment extends Fragment implements OneSignal.NotificationReceivedHandler {

    ListView list;
    ArrayList<LogHolder> msgs;
    ArrayList<LogHolder> rcvd;
    Switch togglebtn;
    LogListAdapter adapter;
    LogListAdapter radapter;
    TextView logtitle;
    GraphView graph;

    public LogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_log, container, false);
        list = (ListView) v.findViewById(R.id.list);
        togglebtn = (Switch) v.findViewById(R.id.togglebtn);
        logtitle = (TextView) v.findViewById(R.id.logtitle);
        logtitle.setText("Received APOM's");
        msgs = new ArrayList<>();
        rcvd = new ArrayList<>();
        adapter = new LogListAdapter(msgs,getActivity());
        radapter = new LogListAdapter(rcvd, getActivity());
        list.setAdapter(radapter);
        graph = (GraphView) v.findViewById(R.id.graph);

        togglebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (togglebtn.isChecked()){

                    list.setAdapter(radapter);
                    logtitle.setText("Received APOM's");

                }
                if (!togglebtn.isChecked()){

                    list.setAdapter(adapter);
                    logtitle.setText("Sent APOM's");

                }
            }
        });

        initAdapters();
        return v;
    }
    @Override
    public void notificationReceived(OSNotification notification) {

        LogHolder logholder;
        String senderid = "";
        DatabaseController dbc =  new DatabaseController();
        try {
            JSONObject obj = notification.payload.additionalData;
            senderid = obj.getString("foo");

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx REP");
        }

        logholder = new LogHolder(senderid, "Inbound");
        System.out.println("£££££££££££££££££££££££ HAPPY DAYS"+logholder.getStatus()+logholder.getDate());
        dbc.createLogInDatabase(logholder);
        System.out.println("£££££££££££££££££££££££ SAD DAYS");

    }

    public void initAdapters(){
        adapter.clear();
        radapter.clear();
        DatabaseController dbc = new DatabaseController();
        String uid = dbc.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("log").child(uid);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        msgs.clear();
                        rcvd.clear();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            System.out.println("!!!!!!!!!!!!!!!!!!!!111"+child.toString());
                            LogHolder lh = child.getValue(LogHolder.class);

                            if(lh.getStatus().equals("Inbound") && lh.getStatus() != null){
                                rcvd.add(lh);
                                System.out.println("!!!!!!!!333"+lh.getStatus());
                            }
                            if (lh.getStatus().equals("Outbound") && lh.getStatus() != null){
                                System.out.println("!!!!!!!!444"+lh.getStatus());
                                msgs.add(lh);

                            }
                            //stack.push(logHolder);
                            System.out.println("!!!!!!!!222"+lh.getStatus());
                        }

                        adapter.notifyDataSetChanged();
                        radapter.notifyDataSetChanged();
                        setupGraph();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    public void setupGraph(){
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new GraphController().parseTime(rcvd);
        LineGraphSeries<DataPoint> series1 = new GraphController().parseTime(msgs);

        series.setTitle("Inbound");
        series1.setTitle("Outbound");
        series.setColor(Color.parseColor("#75a478"));
        series1.setColor(Color.parseColor("#c63f17"));
        series.setDrawDataPoints(true);
        series1.setDrawDataPoints(true);
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.parseColor("#5974a477"));
        graph.addSeries(series);
        graph.addSeries(series1);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 5 because of the space
        graph.getGridLabelRenderer().setHumanRounding(false);
    }
}
