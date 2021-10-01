package com.tolstoy.zurichat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.tolstoy.zurichat.R;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DocumentSentActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 11;
    RecordView recordView;
    RecordButton recordButton;
    ImageView linkBtn, cameaBtn, iconBtn;
    private MediaRecorder mediaRecorder;
    private String audio_path;
    private String sTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_sent);
        linkBtn = findViewById(R.id.link_btn);
        cameaBtn = findViewById(R.id.camera_btn);
        iconBtn = findViewById(R.id.icon_btn);

        recordView = findViewById(R.id.record_view);
        recordButton = findViewById(R.id.record_button);

        recordButton.setRecordView(recordView);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart()   {


                if (!checkPermissionFromDevice()) {
                    linkBtn.setVisibility(View.INVISIBLE);
                    cameaBtn.setVisibility(View.INVISIBLE);
                    iconBtn.setVisibility(View.INVISIBLE);

                    startrecord();
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null){
                        vibrator.vibrate(100);
                    }
                }else {
                    requestPermission();
                }

            }

            @Override
            public void onCancel() {
                try {
                    mediaRecorder.reset();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish(long recordTime) {
                linkBtn.setVisibility(View.VISIBLE);
                cameaBtn.setVisibility(View.VISIBLE);
                iconBtn.setVisibility(View.VISIBLE);

                try {
                    sTime = getTimeText(recordTime);
                    stoprecord();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onLessThanSecond() {
                linkBtn.setVisibility(View.VISIBLE);
                cameaBtn.setVisibility(View.VISIBLE);
                iconBtn.setVisibility(View.VISIBLE);

            }
        });

        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                linkBtn.setVisibility(View.VISIBLE);
                cameaBtn.setVisibility(View.VISIBLE);
                iconBtn.setVisibility(View.VISIBLE);

            }
        });
    }

    private void stoprecord() {
        try {
            if (mediaRecorder != null){
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                //  addVoiceToList(audio_path);
            }else {
                //   Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //  Toast.makeText(getApplicationContext(), "Stop record error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getTimeText(long milliseconds) {
        return String.format("bold",
                TimeUnit.MILLISECONDS.toSeconds(milliseconds),
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                },REQUEST_CODE
        );
    }

    private void startrecord() {
        setUpMediarecorder();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(DocumentSentActivity.this, "recording", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            // Toast.makeText(MainActivity.this, "recording error", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpMediarecorder() {
        String path_save = Environment.getExternalStorageDirectory().getAbsolutePath() +"/" + UUID.randomUUID().toString()+ "audio_ecod";
        audio_path = path_save;

        mediaRecorder = new MediaRecorder();
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(path_save);
        }catch (Exception e){
            Log.d("Tag", e.getMessage());
        }

    }

    private boolean checkPermissionFromDevice(){
        int write_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_storage == PackageManager.PERMISSION_DENIED || record_audio == PackageManager.PERMISSION_DENIED;
    }
}