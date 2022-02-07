package com.example.logalife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

//Contact List
import com.example.logalife.appusage.AppUsageManager;
import com.example.logalife.contacts.ContactManager;
import com.example.logalife.location.LocationManager;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    Context context;

    ContactManager contactManager;
    AppUsageManager appUsageManager;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_main);
        //
        contactManager = new ContactManager(context);
        appUsageManager = new AppUsageManager(context);
        appUsageManager.requestAppStatsPermissions(this);
        locationManager = new LocationManager(context);
        locationManager.requestAppLocationPermissions(this);
    }


    /**
     * Called when the user touches the button
     */
    public void runLogalife(View view) {
        //CONTACTS
        contactManager.getContacts(context);
        contactManager.readPhoneCallRecordList(context);
        //
        //USER STATS
        appUsageManager.getApplicationsUsage(context, false, true);
        //
        //LOCATION
        locationManager.getCurrentLocation();
    }

}