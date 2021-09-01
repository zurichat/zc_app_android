package com.tolstoy.zurichat.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var toolbar: MaterialToolbar
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        toolbar = findViewById(R.id.activity_main_toolbar)
        // call bottomNavigation here
        drawerLayout = findViewById(R.id.draw_layout)
        navigationView = findViewById(R.id.nav_view)


        //get navHostfragment and navController
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_Host) as NavHostFragment
        navController = navHostFrag.navController

        //set up appBarcongi and connect other views to it.
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout) //remove drawerLayout here if using BottomNavigation

        toolbar.setupWithNavController(navController, appBarConfiguration)
        //for BottomNavigation change navigationView to bottomNavigation
        navigationView.setupWithNavController(navController)
    }

    private inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T
    ) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)}
}