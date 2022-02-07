package com.example.logalife.contacts;

import android.graphics.Bitmap;
import android.net.Uri;

public class Contact {
    public String id;
    public String name;
    public String phoneNumber;
    public Bitmap photo;
    public Uri photoURI;

    public Contact(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber.replace("+", "");
    }

    @Override
    public String toString() {
        return this.phoneNumber + " " + this.name;
    }
}
