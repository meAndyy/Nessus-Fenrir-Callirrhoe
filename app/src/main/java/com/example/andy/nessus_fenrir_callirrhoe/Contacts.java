package com.example.andy.nessus_fenrir_callirrhoe;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Andy on 26/01/2018.
 */

public class Contacts {


    public HashMap<String,List<String>> contacts;

    public Contacts(){}

    public Contacts(HashMap<String,List<String>> contacts){
        this.contacts = contacts;
    }

    public HashMap<String,List<String>> getContacts(){
        return contacts;
    }

    public void printContents(){

        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+contacts.keySet().toString());
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+contacts.entrySet().toString());
    }
}
