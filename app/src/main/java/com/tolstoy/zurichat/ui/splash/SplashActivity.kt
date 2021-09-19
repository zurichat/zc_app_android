package com.tolstoy.zurichat.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tolstoy.zurichat.databinding.ActivitySplashBinding
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.login.LoginActivity
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class SplashActivity : AppCompatActivity(), Animation.AnimationListener {

    private var a1: AlphaAnimation? = null
    private var a2: AlphaAnimation? = null
    private var a3: AlphaAnimation? = null

    private lateinit var binding: ActivitySplashBinding

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        lifecycleScope.launch {
            try {
                delay(4000)
                // Navigate to next destination based on user login status
                if (viewModel.isLoggedIn()) {
                    Intent(this@SplashActivity, MainActivity::class.java).run {
                        startActivity(this)
                    }
                } else {
                    Intent(this@SplashActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                }
                finish()
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
        crossFade()
    }

    private fun crossFade() = with(binding) {
        a1 = AlphaAnimation(0f, 1f)
        a1?.duration = 1000
        a2 = AlphaAnimation(1f, 0f)
        a2?.duration = 2000
        a2?.interpolator = AnticipateInterpolator()
        a3 = AlphaAnimation(0f, 1f)
        a3?.duration = 1000
        a1?.setAnimationListener(this@SplashActivity)
        a2?.setAnimationListener(this@SplashActivity)
        a3?.setAnimationListener(this@SplashActivity)
        imageView.visibility = View.VISIBLE
        imageView.animation = a1
    }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        if (animation === a1) {
            binding.imageView.animation = a2
            binding.imageView.visibility = View.INVISIBLE
        }
        if (animation === a2) {
            binding.imageView4.visibility = View.VISIBLE
            binding.imageView4.animation = a3
        }
    }

    override fun onAnimationRepeat(animation: Animation) {}
}