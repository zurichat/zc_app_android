package com.tolstoy.zurichat.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.ui.adapters.OnboardingScreenAdapter;

public class OnboardingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);

        OnboardingScreenAdapter adapter = new OnboardingScreenAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(viewPager);
    }

}