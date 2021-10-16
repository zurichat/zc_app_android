package com.zurichat.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zurichat.app.data.localSource.Cache
import com.zurichat.app.databinding.ActivitySplashBinding
import com.zurichat.app.ui.login.LoginActivity
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.util.setUpApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        lifecycleScope.launch {
            delay(4000)
            if (viewModel.getUserAuthState()) {
                viewModel.user.observe(this@SplashActivity) { user ->
                    val intent = Intent(
                        this@SplashActivity,
                        MainActivity::class.java
                    )
                    Cache.map.putIfAbsent("user", user)
                    intent.putExtra("USER", user)
                    startActivity(intent)
                }
            } else {
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
            }
            finish()
        }
    }

}