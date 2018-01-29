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
    HashMap<String,List<String>> result;

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

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(uid).setValue(user);
    }

    protected void addContacts(String groupname, ArrayList<String> contacts){

        mDatabase.child("users").push().getKey();
        User user = new User();
        Map<String,Object> map = user.toMap(groupname, contacts);
        Map<String,Object> updates = new HashMap<>();
        updates.put("/users/"+uid+"/contacts/"+groupname,map);
        mDatabase.updateChildren(updates);


    }

    protected HashMap<String, List<String>> listeningForContacts(){



        return result;
    }

    protected String getUid(){
        return uid;
    }

    protected DatabaseReference getDatabase(){
        return mDatabase;
    }

}