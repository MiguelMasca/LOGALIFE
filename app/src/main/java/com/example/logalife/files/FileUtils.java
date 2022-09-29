package com.example.logalife.files;

public class FileUtils {

    public static final String IMAGES = "images/";
    public static final String SCREENSHOTS = "screenshots/";
    public static final String VIDEOS = "videos/";
    public static final String FILES = "files/";
    public static final String MUSIC = "music/";
    public static final String[] RELEVANT_DIRECTORIES = {"/DCIM/Camera", "/DCIM/Screenshots", "/Documents", "/Download", "/Movies", "/Music", "/Pictures"};


    public static String getFirebaseStorageDirectoryByExternalDirectory(String externalDirectory) {
        switch (externalDirectory){
            case"/DCIM/Camera":
            case"/Pictures":
                return IMAGES;
            case"/DCIM/Screenshots":
                return SCREENSHOTS;
            case "/Movies":
                return VIDEOS;
            case "/Music":
                return MUSIC;
            default:
                return FILES;
        }
    }
}
