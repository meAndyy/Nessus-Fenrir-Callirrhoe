package com.example.controllers;

import com.example.models.LogHolder;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andy on 16/02/2018.
 */

public class GraphController {

    Date today;
    Date fourdays;
    public GraphController(){

        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        fourdays = calendar.getTime();
        calendar.add(Calendar.DATE, -3);
    }

    public LineGraphSeries<DataPoint>  parseTime(ArrayList<LogHolder> times ){
        int day0 = 0;
        int day1 = 0;
        int day2 = 0;
        int day3 = 0;
        int day4 = 0;
        SimpleDateFormat parser = new SimpleDateFormat("dd-MMM-yyyy  HH:mm ");

        Calendar calendar = Calendar.getInstance();
       // calendar.add(Calendar.HOUR_OF_DAY,12);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        Date d0 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);

       /* LineGraphSeries<DataPoint>  series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d0, day0),
                new DataPoint(d1, day1),
                new DataPoint(d2, day2),
                new DataPoint(d3, day3),
                new DataPoint(d4, day4)
        });*/


        for (int i = 0; i <  times.size();i++){
            LogHolder lh = times.get(i);
            String curr = lh.getDate();
            try {
                Date target = parser.parse(curr);
                System.out.println("$$$$$$$$$xxx"+target);
                if(target.after(d0) && target.before(d1)){
                    day0++;
                }
                if(target.after(d1) && target.before(d2)){
                    day1++;
                }
                if(target.after(d2) && target.before(d3)){
                    day2++;
                }
                if(target.after(d3) && target.before(d4)){
                    day3++;
                }

            }


            catch (ParseException e){
                e.printStackTrace();
            }
            System.out.println("$$$$$$$$$"+day0);
            System.out.println("$$$$$$$$$"+day1);
            System.out.println("$$$$$$$$$"+day2);
            System.out.println("$$$$$$$$$"+day3);
            System.out.println("$$$$$$$$$"+day4);


        }

        LineGraphSeries<DataPoint> series  = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d0, day0),
                new DataPoint(d1, day1),
                new DataPoint(d2, day2),
                new DataPoint(d3, day3),
                new DataPoint(d4, day4)
        });

        return series;
    }

}
