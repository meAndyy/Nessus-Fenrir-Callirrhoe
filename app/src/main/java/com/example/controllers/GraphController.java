package com.example.controllers;

import com.example.models.LogHolder;
import com.jjoe64.graphview.series.BarGraphSeries;
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

    private int day0 ;
    private int day1;
    private int day2;
    private int day3;
    private int day4;
    private Date d4;
    private Date d3;
    private Date d2;
    private Date d1;
    private Date d0;

    public GraphController(ArrayList<LogHolder> times){
        day0 = 0;
        day1 = 0;
        day2 = 0;
        day3 = 0;
        day4 = 0;
        SimpleDateFormat parser = new SimpleDateFormat("dd-MMM-yyyy  HH:mm ");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        d4 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        d3 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        d2 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        d1 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        d0 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);

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



        }

    }

    public LineGraphSeries<DataPoint>  getLineGraph( ){

        LineGraphSeries<DataPoint> series  = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d0, day0),
                new DataPoint(d1, day1),
                new DataPoint(d2, day2),
                new DataPoint(d3, day3),
                new DataPoint(d4, day4)
        });

        System.out.println("$$$$$$$$$*"+day0+""+d0);
        System.out.println("$$$$$$$$$*"+day1+""+d1);
        System.out.println("$$$$$$$$$*"+day2+""+d2);
        System.out.println("$$$$$$$$$*"+day3+""+d3);
        System.out.println("$$$$$$$$$*"+day4+""+d4);

        return series;
    }

    public int getBarGraph( ){

        int sum = day0 + day1 + day2 +day3 + day4;

        return sum;

    }
}
