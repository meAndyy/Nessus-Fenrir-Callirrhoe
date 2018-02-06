package com.example.andy.nessus_fenrir_callirrhoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 20/01/2018.
 */

public class User {

    public String name;
    public String  phone;
    public String email;
    public HashMap<String,Object> contacts = new HashMap<String,Object>();

    public User(){}

    public User(String name, String phone, String email){

        this.name = name;
        this.phone = phone;
        this.email = email;

    }
    public User(String name, String phone, String email,HashMap list){

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.contacts = list;

    }

    public Map<String, Object> toMap(String groupname, ArrayList<String> contacts) {
        HashMap<String, Object> result = new HashMap<>();
        for(int i = 0; i < contacts.size(); i++){

            result.put(groupname+i, contacts.get(i));
            System.out.println(""+groupname+" "+contacts.get(i));
        }

        return result;
    }
}
