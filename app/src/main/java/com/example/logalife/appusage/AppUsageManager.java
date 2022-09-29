package com.example.logalife.appusage;


import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.util.Log;

import com.example.logalife.utils.DateUtils;
import com.example.logalife.utils.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AppUsageManager {

    Context context;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public AppUsageManager(Context context) {
        this.context = context;
    }


    public void requestPermissions(Activity activity) {
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) activity.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis());
        boolean isEmpty = stats.isEmpty();
        if (isEmpty) {
            activity.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    public HashMap<String, Long> getApplicationsUsage(Context context, boolean showUnusedApps, boolean isDebugMode ) {
        //
        List<UsageStats> stats = getRawStats(context);
        HashMap<String,Long> appMinutes = new HashMap<>();
        DatabaseReference appUsageRef = databaseReference.child("app_usage");
        //
        for (UsageStats stat: stats) {
            //
            String validPackageName = FirebaseUtils.replaceDotsWithUnderscore(stat.getPackageName());
            //
            PackageManager packageManager= context.getPackageManager();
            //
            long registeredMillis = (appMinutes.containsKey(validPackageName) ? appMinutes.get(validPackageName) : 0) ;
            //
            //Show only unused apps if user wants to, otherwise only if they were used over a minute
            if(showUnusedApps || stat.getTotalTimeInForeground() > DateUtils.ONE_MINUTE_IN_MILLIS){
                appMinutes.put(validPackageName,registeredMillis + stat.getTotalTimeInForeground());
            }
        }
        //
        if(isDebugMode){
            appMinutes.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                    .forEach(app -> Log.i("APP STATS:", getAppNameFromPackage(app.getKey(), context) + " TIME VISIBLE: " + DateUtils.convertmillisToHoursAndMins(app.getValue())));
        }
        //
        //Store App Name in Firebase:
        appUsageRef.setValue(appMinutes);
        //
        return appMinutes;
    }

    private List<UsageStats> getRawStats(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        long start = calendar.getTimeInMillis()-100000000;
        long end = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, start, end);
    }


    private String getAppNameFromPackage(String packageName, Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //
        List<ResolveInfo> pkgAppsList = context.getPackageManager()
                .queryIntentActivities(mainIntent, 0);
        //
        for (ResolveInfo app : pkgAppsList) {
            if (app.activityInfo.packageName.equals(packageName)) {
                return app.activityInfo.loadLabel(context.getPackageManager()).toString();
            }
        }
        //
        return packageName;
    }


}
