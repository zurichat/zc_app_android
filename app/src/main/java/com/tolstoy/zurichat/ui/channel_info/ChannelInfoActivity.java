package com.tolstoy.zurichat.ui.channel_info;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.models.Media;
import com.tolstoy.zurichat.models.Participant;
import com.tolstoy.zurichat.models.User;
import com.tolstoy.zurichat.ui.adapters.MediaAdapter;
import com.tolstoy.zurichat.ui.adapters.ParticipantAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChannelInfoActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_info);

        navController = Navigation.findNavController(this, R.id.nav_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A61DB")));

        String label = (String) navController.getCurrentDestination().getLabel();

        try {
            Intent intent = getIntent();
            if (intent != null){
                String channel = intent.getStringExtra("channel_name");
                int numberOfDocument = intent.getIntExtra("number_of_document",0);
                List<User> members = intent.getParcelableArrayListExtra("members");

            }else{
                System.out.println("bundle is null");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


        if (actionBar != null) {
            try {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(label);
            } catch (Exception e) {

            }
        }


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#1A61DB"));
    }

}