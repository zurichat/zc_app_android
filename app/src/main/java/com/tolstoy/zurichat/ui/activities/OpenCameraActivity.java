package com.tolstoy.zurichat.ui.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tolstoy.zurichat.R;

public class OpenCameraActivity extends AppCompatActivity {

    public static final int CAMERA_ACTION_CODE = 1;
    ImageView imageView;
    ImageView imageButton;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_camera);

        imageView = findViewById(R.id.displayid_ImgV);
        imageButton = findViewById(R.id.camera_btn);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bitmap);
            }
        });

        // click listener for the camera button to start the camera
        imageButton.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null){
                activityResultLauncher.launch(intent);
            }else{
                Toast.makeText(OpenCameraActivity.this, "Unable to Start Camera!", Toast.LENGTH_SHORT).show();
            }
        });

        }

    }
