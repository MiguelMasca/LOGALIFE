package com.example.logalife.utils;

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

}
