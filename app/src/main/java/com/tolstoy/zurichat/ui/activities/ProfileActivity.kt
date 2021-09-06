package com.tolstoy.zurichat.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.tolstoy.zurichat.R
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.IOException import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class ProfileActivity: AppCompatActivity() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val PERMISSION_REQUEST_CODE = 101
    }

    private var mCurrentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img_camera.setOnClickListener(View.OnClickListener {
            if (checkPermission()) takePhoto() else requestPermission()
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takePhoto()
                } else {
                    Toast.makeText(this, "Permission Denied",Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {

            }
        }
    }

    private fun takePhoto() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this, "com.tolstoy.zurichat.fileprovider",
            file)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            val auxFile = File(mCurrentPhotoPath)

            var bitmap: Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            display_picture.setImageBitmap(bitmap)
        }
    }

    private fun checkPermission():Boolean {
        return(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ), PERMISSION_REQUEST_CODE)
    }

    @Throws(IOException::class)
    private fun createFile(): File {

        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            mCurrentPhotoPath = absolutePath
        }
    }
}