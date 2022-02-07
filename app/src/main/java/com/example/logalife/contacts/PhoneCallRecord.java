package com.example.logalife.contacts;

import android.graphics.Bitmap;
import android.net.Uri;

import java.time.Instant;
import java.util.Date;

public class PhoneCallRecord {
    public String id;
    public Contact contact;
    public String phoneNumber;
    public String contactName;
    public Date date;
    public long timestamp;
    public String duration;
    public String type;

    public PhoneCallRecord(String phoneNumber, String contactName, long timestamp, String duration, String type) {
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.timestamp = timestamp;
        this.date = Date.from(Instant.ofEpochSecond(timestamp));
        this.duration = duration;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.phoneNumber + " " + this.contactName + " " + this.duration + " " + this.type + " " + this.date;
    }
}
