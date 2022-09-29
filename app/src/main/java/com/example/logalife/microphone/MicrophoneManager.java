package com.example.logalife.microphone;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.logalife.MainActivity;
import com.example.logalife.utils.DateUtils;

import java.io.IOException;

public class MicrophoneManager {

    private static MicrophoneManager microphoneManager = null;

    Context context;

    private String fileName;
    private MediaRecorder recorder;

    Boolean isRecording;

    public static MicrophoneManager getInstance()
    {
        if (microphoneManager == null)
            microphoneManager = new MicrophoneManager();

        return microphoneManager;
    }

    private MicrophoneManager() {
        this.context = MainActivity.getContext();
        this.isRecording = false;
    }

    public void toogleMicrophone(Activity activity) throws IOException {
        if(!isRecording){
            isRecording = true;
            startRecording();
        }else{
            isRecording = false;
            stopRecording();
        }
    }

    public void startRecording() {
        fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + DateUtils.getDateInFileFormat() + ".3gp";
        Log.d("RECORDING", " - filename: " + fileName);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setMaxDuration(500000000);
        recorder.setMaxFileSize(500000000);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.d("RECORDING", "prepare() failed - " + e.getMessage());
        }

        recorder.start();
        Toast.makeText(context, "Recording Started", Toast.LENGTH_SHORT).show();
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();     // stop recording
            recorder.reset();    // set state to idle
            recorder.release();  // release resources back to the system
            recorder = null;
            Toast.makeText(context, "Recording Stopped", Toast.LENGTH_SHORT).show();
        }
    }
}
