package com.tolstoy.zurichat.ui.login

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.login.screens.InternetFragment
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        //internet connection
        internetConnection()
        internetConnectionChecking()
    }


    //Internet Connectivity checking on starting the app
    private fun internetConnection()
    {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if(!isConnected){
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_placeholder, InternetFragment())
            ft.commit()
        }
    }
    // Internet Connectivity checking
    private fun internetConnectionChecking()
    {
        val cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        cm.registerNetworkCallback(
            builder.build(), object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)

                    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_placeholder)
                    if(fragment != null)
                    {
                        val ft = supportFragmentManager.beginTransaction()
                        ft.remove(fragment)
                        ft.commit()
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.add(R.id.fragment_placeholder, InternetFragment())
                    ft.commit()
                }
            }
        )
    }
}