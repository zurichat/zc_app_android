package com.tolstoy.zurichat.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.ui.adapters.OnboardingScreenAdapter;

public class OnboardingScreenActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private OnboardingScreenAdapter adapter;
    private DotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);

        adapter = new OnboardingScreenAdapter(getSupportFragmentManager(), this);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        dotsIndicator =  findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(viewPager);
    }
}