package com.tolstoy.zurichat.ui.fragments.networking;

import android.app.Activity;
import android.widget.Toast;

import com.tolstoy.zurichat.ui.fragments.viewmodel.SharedChannelViewModel;

import centrifuge.Client;
import centrifuge.ServerPublishEvent;
import centrifuge.ServerPublishHandler;

public class AppServerPublishHandler implements ServerPublishHandler {
    protected Activity context;
    private SharedChannelViewModel sharedChannelViewModel;

    public AppServerPublishHandler(Activity context, SharedChannelViewModel sharedChannelViewModel) {
        this.context = context;
        this.sharedChannelViewModel = sharedChannelViewModel;
    }

    @Override
    public void onServerPublish(Client client, ServerPublishEvent serverPublishEvent) {
        Toast.makeText(context,"Publshed",Toast.LENGTH_SHORT).show();
    }
}
