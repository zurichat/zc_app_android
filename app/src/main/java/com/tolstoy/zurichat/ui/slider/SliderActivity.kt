package com.tolstoy.zurichat.ui.slider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivitySliderBinding
import com.tolstoy.zurichat.model.slide
import com.tolstoy.zurichat.ui.adapters.sliderAdapter
import com.tolstoy.zurichat.ui.login.LoginActivity

class SliderActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    val pref_show_intro = "Intro"


    private lateinit var binding: ActivitySliderBinding
    private val SliderAdapter = sliderAdapter(
        listOf(
            slide(
                "Zuri Channel",
                "Features of channels",
                R.drawable.splash_logo
            ),
            slide(
                "Zuri DMS",
                "Features of personal chatting",
                R.drawable.splash_logo
            ),
            slide(
                "Zuri Chat",
                "Features of chatting with business personal",
                R.drawable.splash_logo
            )
        )

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySliderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("introSlider", Context.MODE_PRIVATE)

        //Check For First time Usage
        if (!preferences.getBoolean(pref_show_intro, true)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.sliderViewPager.adapter = SliderAdapter
        setupIndicator()
        setCurrentIndicator(0)
        binding.sliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        binding.buttonNext.setOnClickListener {
            if (binding.sliderViewPager.currentItem + 1 < SliderAdapter.itemCount) {
                binding.sliderViewPager.currentItem += 1
            } else {
                Intent(applicationContext, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()

                    val editor = preferences.edit()
                    editor.putBoolean(pref_show_intro, false).apply()
                }
            }
        }
        binding.textSkipIntro.setOnClickListener {
            Intent(applicationContext, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun setupIndicator() {
        val indicators = arrayOfNulls<ImageView>(SliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.splash_logo
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.splash_logo
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.splash_logo
                    )
                )
            }
        }
    }
}