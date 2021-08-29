package com.tolstoy.zurichat.slider

import android.content.Intent
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
import com.tolstoy.zurichat.ui.login.LoginActivity

class SliderActivity : AppCompatActivity() {

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