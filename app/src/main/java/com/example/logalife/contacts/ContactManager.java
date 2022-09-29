package com.example.logalife.contacts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.logalife.utils.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ContactManager {

    Context context;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public ContactManager(Context context) {
        this.context = context;
    }

    public void requestPermissions(Activity activity) {
        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_CONTACTS}, 101);
            }
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_CALL_LOG}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public HashMap<String, Contact> getContacts(Context context) {
        //
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //
        if (cursor.getCount() == 0) {
            Log.d("SIZE 0 CHECK", "NO CONTACTS FOUND IN THE DEVICE");
        }
        //
        HashMap<String, Contact> contactList = new HashMap<>();
        DatabaseReference contactsRef = databaseReference.child("contacts");
        //
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                //
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                //
                while (cursorInfo.moveToNext()) {
                    //
                    //Create and Store contact (if it is a valid one)
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phoneNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //
                    if (FirebaseUtils.isValidKey(phoneNumber)) {
                        Contact contact = new Contact(id, name, phoneNumber);
                        Log.i("CONTACT", contact.toString());
                        contactList.put(contact.phoneNumber, contact);
                    }
                    //
                }
                cursorInfo.close();
            }
        }
        cursor.close();
        //
        //Store Contact List in Firebase:
        contactsRef.setValue(contactList);
        return contactList;
    }

    @SuppressLint("Range")
    public HashMap<String,PhoneCallRecord> readPhoneCallRecordList(Context context) {
        Uri uriCallLogs = Uri.parse("content://call_log/calls");
        Cursor cursorCallLogs = context.getContentResolver().query(uriCallLogs, null, null, null);
        cursorCallLogs.moveToFirst();
        //
        HashMap<String, PhoneCallRecord> phoneCallRecordList = new HashMap<>();
        DatabaseReference phoneCallRecordRef = databaseReference.child("call_records");
        //
        do {
            String phoneNumber = FirebaseUtils.removePlusSign(cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.NUMBER)));
            String contactName = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String duration = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.DURATION));
            String type = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.TYPE));
            long timestamp = cursorCallLogs.getLong(cursorCallLogs.getColumnIndex(CallLog.Calls.DATE));
            //
            if (FirebaseUtils.isValidKey(phoneNumber)) {
                PhoneCallRecord phoneCallRecord = new PhoneCallRecord(phoneNumber,contactName,timestamp, duration,type);
                //Log.i("CONTACT RECORD", phoneCallRecord.toString());
                phoneCallRecordList.put( phoneCallRecord.phoneNumber, phoneCallRecord);
                Log.i("CONTACT RECORD", phoneCallRecord.phoneNumber + " " + phoneCallRecord.contactName);
            }
            //
        } while (cursorCallLogs.moveToNext());

        // Group and Order Call records by day
        /*phoneCallRecordList.entrySet().stream().sorted((k1, k2) -> -k1.getValue().timestamp.compareTo(k2.getValue().timestamp))
                .forEach(call -> Log.i("KEY ORDERED:", call.getKey() + " - " + call.getValue()));*/
        //
        //Store Call Record in Firebase:
        phoneCallRecordRef.setValue(phoneCallRecordList);
        return phoneCallRecordList;
    }
}
