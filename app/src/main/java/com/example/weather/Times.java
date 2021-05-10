package com.example.weather;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class Times {
    private static String TAG = "Times";

    public static String getFormatCurrentTime(){
        long currentTime = new Date().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH dd MM yyyy");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(currentTime);
    }


    public static String convertUnixToDate(long dt, String s, int offset) {
        SimpleDateFormat sdf;
        Date date = new Date(dt*1000L);

        switch (s){
            case "hour" :
                sdf = new SimpleDateFormat("HH dd MM yyyy");
                break;
            case "day" :
                sdf = new SimpleDateFormat("dd MM yyyy");
                break;
            case "display" :
                sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                break;
            case "displayHour" :
                sdf = new SimpleDateFormat("HH:mm");
                break;
            default :
                return "null";
        }
        String[] ids = TimeZone.getAvailableIDs(offset*1000);
        Log.e(TAG, ids[0]);
        sdf.setTimeZone(TimeZone.getTimeZone(ids[0]));
        return sdf.format(date);
    }


}
