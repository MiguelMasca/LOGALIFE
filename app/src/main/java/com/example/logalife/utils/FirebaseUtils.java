package com.example.logalife.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseUtils {

    public static boolean isValidKey(String key) {
        boolean isValidKey = true;
        isValidKey = isValidKey && key.length() > 0;
        isValidKey = isValidKey && !key.contains("/");
        isValidKey = isValidKey && !key.contains(".");
        isValidKey = isValidKey && !key.contains("#");
        isValidKey = isValidKey && !key.contains("$");
        isValidKey = isValidKey && !key.contains("[");
        isValidKey = isValidKey && !key.contains("]");
        //
        //
        return isValidKey;
    }

    public static String removePlusSign(String number) {
        return number.replace("+", "");
    }

    public static String replaceDotsWithUnderscore(String key) {
        return key.replace(".", "_");
    }

    public static String replaceUnderScoreWithDot(String key) {
        return key.replace("_", ".");
    }

    public static void logToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("TOKEN", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("TOKEN", token);
                    }
                });
    }
}
