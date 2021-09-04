package com.tolstoy.zurichat.ui.otp;

import static com.tolstoy.zurichat.util.UtilitiesKt.setUpApplicationTheme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tolstoy.zurichat.R;

public class OTP_Page extends AppCompatActivity {
    private Button otpVerifyCodeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this);

        otpVerifyCodeBtn = findViewById(R.id.otpVerifyCodeBtn);
        otpVerifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                startActivity(intent);
            }
        });
    }
}