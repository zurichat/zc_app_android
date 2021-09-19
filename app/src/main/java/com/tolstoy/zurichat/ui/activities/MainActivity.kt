package com.tolstoy.zurichat.ui.activities


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.tolstoy.zurichat.databinding.ActivityMainBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.login.screens.LoginFragmentDirections
import com.tolstoy.zurichat.util.setUpApplicationTheme

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        //Request permission for accessing media and files from storage
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            //Shows Toast message if permission is granted or denied.
            if (isGranted) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
            }
        }

        //This code runs automatically,
        //This checks if the permission has been granted, if it has, pass, else, it request for the permission using the function above
        //Comment this if and else statements to prevent permission from showing on startup.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Pass
        } else {
            //Request permission
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

}

