package com.zurichat.app.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zurichat.app.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateOrganizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organization);
    }
}