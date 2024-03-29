package com.example.logalife.location;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class LocationManager {

    private FusedLocationProviderClient fusedLocationClient;
    Context context;

    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public  LocationManager(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void requestPermissions(Activity activity) {
        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCurrentLocation() {
        // Request permission
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Main code
            Task<Location> currentLocationTask = fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken());

            currentLocationTask.addOnCompleteListener((new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful() && task.getResult() != null) {
                        // Task completed successfully
                        saveLocationInfoInFirebase(task.getResult());
                    } else {
                        // Task failed with an exception
                        Exception exception = task.getException();
                        Log.d("ERROR RETRIEVING LOCATION: ", "Exception thrown: " + exception);
                    }
                }
            }));
        } else {
            //Request fine location permission
            Log.d("LOCATION", "Request fine location permission.");
        }
    }

    public static void saveLocationInfoInFirebase(Location location){

        FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = fDatabase.getReference();

        LocalDateTime date = LocalDateTime.now();
        DecimalFormat dmFormat= new DecimalFormat("00");
        String month = dmFormat.format(Double.valueOf(date.getMonthValue()));
        String day = dmFormat.format(Double.valueOf(date.getDayOfMonth()));
        String hours = dmFormat.format(Double.valueOf(date.getHour()));
        String minutes = dmFormat.format(Double.valueOf(date.getMinute()));
        String seconds = dmFormat.format(Double.valueOf(date.getSecond()));
        DatabaseReference locationRef = dbReference.child("location").child("" + date.getYear()).child("" + month).child("" + day);

        LocationInfo locationInfo = new LocationInfo(System.currentTimeMillis(), location.getLatitude(),location.getLongitude(),location.getAltitude());
        locationRef.child(hours + ":" + "" + minutes + ":" + seconds).setValue(locationInfo);
        Log.d("SAVING LOCATION: ", "Location: " + location.getLatitude() + ", " + location.getLongitude());
    }

}
