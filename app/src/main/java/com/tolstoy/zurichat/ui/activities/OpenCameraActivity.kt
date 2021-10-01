package com.tolstoy.zurichat.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.tolstoy.zurichat.R
import java.util.jar.Manifest


class OpenCameraActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_camera)
        val cameraButton = findViewById<ImageView>(R.id.camera_btn)

        cameraButton.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    111
                )
                Toast.makeText(applicationContext, "Unable to start camera", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()

            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i,101)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
        }
    }


}