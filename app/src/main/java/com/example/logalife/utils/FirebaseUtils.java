package com.example.logalife.utils;

public class FirebaseUtils {

    public static boolean isValidKey(String key) {
        boolean isValidKey = true;
        isValidKey = isValidKey && key.length() > 0;
        isValidKey = isValidKey && ! key.contains("/");
        isValidKey = isValidKey && ! key.contains(".");
        isValidKey = isValidKey && ! key.contains("#");
        isValidKey = isValidKey && ! key.contains("$");
        isValidKey = isValidKey && ! key.contains("[");
        isValidKey = isValidKey && ! key.contains("]");
        //
        //
        return isValidKey;
    }

    public static String removePlusSign(String number) {
        return number.replace("+","");
    }

    public static String replaceDotsWithUnderscore(String key) {
        return key.replace(".","_");
    }

    public static String replaceUnderScoreWithDot(String key) {
        return key.replace("_",".");
    }
}
