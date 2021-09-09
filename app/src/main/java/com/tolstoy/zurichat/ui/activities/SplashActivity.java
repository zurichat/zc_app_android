package com.tolstoy.zurichat.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.ui.login.LoginActivity;
import com.tolstoy.zurichat.util.UtilitiesKt;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    private View image1;
    private View image2;
    private AlphaAnimation a1, a2, a3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // This setups application theme to value stored in sharedPref
        UtilitiesKt.setUpApplicationTheme(this);

        image1 = findViewById(R.id.imageView);
        image2 = findViewById(R.id.imageView4);


        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception ignored) {
                }
            }
        };
        thread.start();

        crossFade();


    }

    private void crossFade() {

        a1 = new AlphaAnimation(0f, 1f);
        a1.setDuration(1000);
        a2 = new AlphaAnimation(1f, 0f);
        a2.setDuration(2000);
        a2.setInterpolator(new AnticipateInterpolator());
        a3 = new AlphaAnimation(0f, 1f);
        a3.setDuration(1000);

        a1.setAnimationListener(this);
        a2.setAnimationListener(this);
        a3.setAnimationListener(this);

        image1.setVisibility(View.VISIBLE);
        image1.setAnimation(a1);

    }

    @Override
    public void onAnimationStart(Animation animation) {


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == a1) {
            image1.setAnimation(a2);
            image1.setVisibility(View.INVISIBLE);
        }

        if (animation == a2) {
            image2.setVisibility(View.VISIBLE);
            image2.setAnimation(a3);

        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}