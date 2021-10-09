package com.zurichat.app.ui.channel_info;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zurichat.app.R;
import com.zurichat.app.ui.fragments.model.ChannelData;
import com.zurichat.app.ui.fragments.viewmodel.ChannelMessagesViewModel;

public class ChannelInfoActivity extends AppCompatActivity {

    private NavController navController;
    ChannelMessagesViewModel channelMessagesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_info);

        channelMessagesViewModel = new ViewModelProvider(this).get(ChannelMessagesViewModel.class);
        navController = Navigation.findNavController(this, R.id.nav_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A61DB")));

        String label = (String) navController.getCurrentDestination().getLabel();

        channelMessagesViewModel.getChannelData().observe(this, channelData -> {
            if (channelData != null) {
                getDataPass(channelData);
            }
        });
//
//        try {
//            Intent intent = getIntent();
//            if (intent != null) {
//                String channel = intent.getStringExtra("channel_name");
//                int numberOfDocument = intent.getIntExtra("number_of_document", 0);
//                List<User> members = intent.getParcelableArrayListExtra("members");
//
//            } else {
//                System.out.println("bundle is null");
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


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

    //implement the channel data to view
    private void getDataPass(ChannelData channelData) {
        System.out.println("Channel Name "+channelData.getChannelName());
        System.out.println("Channel channel "+channelData.getMembers());
        System.out.println("Channel data "+channelData.toString());
    }

}