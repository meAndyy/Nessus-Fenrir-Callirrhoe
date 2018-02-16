package com.example.andy.nessus_fenrir_callirrhoe;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 25/01/2018.
 */

public class DatabaseController {
    protected FirebaseUser currentFirebaseUser;
    protected DatabaseReference mDatabase;
    protected FirebaseAuth auth;
    String uid;


    public DatabaseController(){

        uid="";
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentFirebaseUser = auth.getCurrentUser();
        if(auth.getCurrentUser() != null) {
            uid = currentFirebaseUser.getUid();

        }

    }


    protected void createUserInDatabase(User user){

        mDatabase.child("users").child(uid).setValue(user);
    }

    protected void createLogInDatabase(LogHolder logHolder){

        mDatabase.child("log").child(uid).push().setValue(logHolder);
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx CRITICAL SENTTTTTTTTTTT");
    }

    protected void addContacts(String groupname, ArrayList<String> contacts){

        mDatabase.child("users").push().getKey();
        User user = new User();
        Map<String,Object> map = user.toMap(groupname, contacts);
        Map<String,Object> updates = new HashMap<>();
        updates.put("/users/"+uid+"/contacts/"+groupname,map);
        mDatabase.updateChildren(updates);


    }


    protected void deleteData(String del){

        mDatabase.child("users").child(uid).child("contacts").child(del).removeValue();
    }

    /*protected void addToLog(){
        mDatabase.child("log").push().getKey();
        User user = new User();
        Date date = new Date();
        Map<String,Object> map = user.toMap("msg", date.toString());
        Map<String,Object> updates = new HashMap<>();
        updates.put("/users/"+uid+groupname,map);
        mDatabase.updateChildren(updates);
    }*/



    protected String getUid(){
        return uid;
    }

    protected DatabaseReference getDatabase(){
        return mDatabase;
    }

}
