<<<<<<< HEAD:app/src/main/java/com/tolstoy/zurichat/ui/splash/SplashActivity.java
package com.tolstoy.zurichat.ui.splash;
=======
package com.tolstoy.zurichat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
>>>>>>> 0b21b703117800eadc3d318923d586d431ef1e1f:app/src/main/java/com/tolstoy/zurichat/ui/activities/SplashActivity.java

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tolstoy.zurichat.R;
<<<<<<< HEAD:app/src/main/java/com/tolstoy/zurichat/ui/splash/SplashActivity.java
import com.tolstoy.zurichat.slider.SliderActivity;
=======
import com.tolstoy.zurichat.ui.slider.SliderActivity;
>>>>>>> 0b21b703117800eadc3d318923d586d431ef1e1f:app/src/main/java/com/tolstoy/zurichat/ui/activities/SplashActivity.java

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