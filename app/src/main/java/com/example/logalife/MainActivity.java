package com.example.logalife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

//Contact List
import com.example.logalife.appusage.AppUsageManager;
import com.example.logalife.contacts.ContactManager;
import com.example.logalife.files.FileManager;
import com.example.logalife.location.LocationUtils;
import com.example.logalife.location.LocationManager;
import com.example.logalife.location.LocationService;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    Context context;

    ContactManager contactManager;
    AppUsageManager appUsageManager;
    LocationManager locationManager;
    FileManager fileManager;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        /*Intent intent = new Intent(this, ForegroundService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }*/
        //
        context = this.getApplicationContext();
        setContentView(R.layout.activity_main);
        //

        findViewById(R.id.buttonStartLocationUpdates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    startLocationService();
                }
            }
        });

        findViewById(R.id.buttonStopLocationUpdates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
            }
        });

        //
        /*contactManager = new ContactManager(context);
        contactManager.requestPermissions(this);
        //
        appUsageManager = new AppUsageManager(context);
        appUsageManager.requestPermissions(this);
        //
        locationManager = new LocationManager(context);
        locationManager.requestPermissions(this);*/
        //
        fileManager = new FileManager(context);
        //
    }


    public Activity getActivity() {
        return this;
    }

    /**
     * Called when the user touches the button
     */
    public void runLogalife(View view)  {
        //
        fileManager.requestPermissions(MainActivity.this);

        /*contactManager.requestPermissions(this);
        appUsageManager.requestPermissions(this);
        locationManager.requestPermissions(this);
        //
        //CONTACTS
        /*contactManager.getContacts(context);
        contactManager.readPhoneCallRecordList(context);
        //
        //USER STATS
        appUsageManager.getApplicationsUsage(context, false, true);*/
        //
        //LOCATION
        //locationManager.getCurrentLocation();
        //
        //FILES
        fileManager.getFiles();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName()) && service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationUtils.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationUtils.ACTION_STOP_LOCATION_SERVICE);
            stopService(intent);
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
        }
    }


}