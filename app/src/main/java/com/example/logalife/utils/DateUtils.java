package com.example.logalife.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static long ONE_MINUTE_IN_MILLIS = 60000;

    public static String convertmillisToHoursAndMins(Long millis) {
        //
        long seconds = ((millis%(1000*60*60))%(1000*60))/1000;
        long hours = millis/(1000*60*60);
        long minutes = (millis%(1000*60*60))/(1000*60);
        //
        return hours + "h : " + minutes+ "m : " + seconds + "s";
    }


    public static String getDateInFileFormat(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd___HH_mm_ss");
        Date date = new Date();
        return formatter.format(date);
    }

}
