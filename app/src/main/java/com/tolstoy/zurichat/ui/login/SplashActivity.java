package com.tolstoy.zurichat.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.slider.SliderActivity;
import com.tolstoy.zurichat.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    Intent i = new Intent(getBaseContext(), SliderActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception ignored) {}
            }
        };
        thread.start();
    }
}