package com.example.andy.nessus_fenrir_callirrhoe;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andy on 07/02/2018.
 */

public class LogHolder {

    String name;
    String date;

    public String getStatus() {
        return status;
    }

    String status;

    public LogHolder(){}

    public LogHolder(String name, String status){
        this.name = name;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy  HH:mm ");
        this.date = df.format(c.getTime());
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

}
