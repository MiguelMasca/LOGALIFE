package com.example.logalife.location;

import android.graphics.Bitmap;
import android.net.Uri;

public class LocationInfo {
    public long timestamp;
    public double latitude;
    public double longitude;
    public double altitude;

    public LocationInfo(long timestamp, double latitude, double longitude, double altitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return this.latitude + " " + this.longitude;
    }
}
