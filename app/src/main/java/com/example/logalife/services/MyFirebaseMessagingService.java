package com.example.logalife.services;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.logalife.MainActivity;
import com.example.logalife.location.LocationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String START_SERVICE = "START_SERVICE";
        String STOP_SERVICE = "STOP_SERVICE";

        Log.d("NOTIFICATION", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            String action = remoteMessage.getData().get("action");
            Log.d("NOTIFICATION", "ACTION: " + action);

            if (action.equals(START_SERVICE)) {
                Intent mainActivity = new Intent(this, MainActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainActivity.setFlags(Intent.FLAG_FROM_BACKGROUND);
                startActivity(mainActivity);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) MainActivity.getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    startForegroundService();
                }
            } else if(action.equals(STOP_SERVICE)) {
                stopForegroundService();
            }
        }
    }


    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (ForegroundService.class.getName().equals(service.service.getClassName()) && service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startForegroundService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), ForegroundService.class);
            intent.setAction(LocationUtils.ACTION_START_LOCATION_SERVICE);
            startForegroundService(intent);
            //Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopForegroundService() {
        Intent intent = new Intent(getApplicationContext(), ForegroundService.class);
        intent.setAction(LocationUtils.ACTION_STOP_LOCATION_SERVICE);
        stopService(intent);
        //Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
    }


}
