package com.example.controllers;

import com.example.models.LogHolder;
import com.example.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 25/01/2018.
 */

    public class DatabaseController {
    public FirebaseUser currentFirebaseUser;
    public DatabaseReference mDatabase;
    public FirebaseAuth auth;
    String uid;
    String senderid;


    public DatabaseController(){

        uid="";
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentFirebaseUser = auth.getCurrentUser();
        if(auth.getCurrentUser() != null) {
            uid = currentFirebaseUser.getUid();
            senderid = currentFirebaseUser.getEmail();

        }

    }


     public void createUserInDatabase(User user){

        mDatabase.child("users").child(uid).setValue(user);
    }

    public void createLogInDatabase(LogHolder logHolder){

        mDatabase.child("log").child(uid).push().setValue(logHolder);
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx CRITICAL SENTTTTTTTTTTT");
    }

    public void addContacts(String groupname, ArrayList<String> contacts){

        mDatabase.child("users").push().getKey();
        User user = new User();
        Map<String,Object> map = user.toMap(groupname, contacts);
        Map<String,Object> updates = new HashMap<>();
        updates.put("/users/"+uid+"/contacts/"+groupname,map);
        mDatabase.updateChildren(updates);


    }

    public String getSenderid(){
        return senderid;
    }


    public void deleteData(String del){

        mDatabase.child("users").child(uid).child("contacts").child(del).removeValue();
    }

    /*public void addToLog(){
        mDatabase.child("log").push().getKey();
        User user = new User();
        Date date = new Date();
        Map<String,Object> map = user.toMap("msg", date.toString());
        Map<String,Object> updates = new HashMap<>();
        updates.put("/users/"+uid+groupname,map);
        mDatabase.updateChildren(updates);
    }*/



    public String getUid(){
        return uid;
    }

    public DatabaseReference getDatabase(){
        return mDatabase;
    }

}
