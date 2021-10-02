package com.tolstoy.zurichat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tolstoy.zurichat.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateOrganizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organization);
    }
}